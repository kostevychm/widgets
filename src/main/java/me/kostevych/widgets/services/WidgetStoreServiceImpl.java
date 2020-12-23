package me.kostevych.widgets.services;

import me.kostevych.widgets.exceptions.WidgetNotFoundException;
import me.kostevych.widgets.model.Widget;
import me.kostevych.widgets.model.WidgetRequest;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Component
public class WidgetStoreServiceImpl implements WidgetStoreService {

    private final LinkedList<Widget> widgetsRepo = new LinkedList<>();

    ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    @Override
    public Widget add(WidgetRequest request) {
        lock.writeLock().lock();
        Widget widget = new Widget(request);
        widget.setModifiedDate(LocalDateTime.now());
        widget.setId(UUID.randomUUID());
        pushIntoStore(widget, request.getZ() == null);
        lock.writeLock().unlock();
        return widget;
    }

    private void pushIntoStore(Widget widget, boolean zIsZero) {
        //When should be added to the end
        if(zIsZero) {
            widget.setZ(widgetsRepo.isEmpty()?0:widgetsRepo.getLast().getZ()+1);
        }
        //Could be stored now without reshuffle
        if(widgetsRepo.isEmpty() || widgetsRepo.getLast().getZ() < widget.getZ()) {
            widgetsRepo.add(widget);
        } else {
            ListIterator<Widget> iterator = widgetsRepo.listIterator();
            boolean alreadyInserted = false;
            while (iterator.hasNext()) {
                Widget currentWidget = iterator.next();

                //Insert before first higher z-index
                if(!alreadyInserted && currentWidget.getZ() >= widget.getZ()) {
                    iterator.previous();
                    iterator.add(widget);
                    alreadyInserted = true;
                    iterator.next(); //Go back to checked element
                }

                //If inserted look to previous
                if(alreadyInserted) {
                    Widget previous = iterator.previous();
                    if(previous.getZ() == currentWidget.getZ()) { //when current and previous are equal
                        currentWidget.incrementZ(); //current one should be incremented
                        iterator.next(); //move forward
                    } else { // Gaps are allowed, when next one is already higher no need to increment all records
                        break;
                    }
                }
            }
        }
    }

    @Override
    public Widget get(UUID id) {
        lock.readLock().lock();
        for(Widget res : widgetsRepo) {
            if (res.getId().equals(id)) {
                lock.readLock().unlock();
                return res;
            }
        }
        lock.readLock().unlock();
        throw new WidgetNotFoundException(id);
    }

    @Override
    public Widget edit(UUID id, WidgetRequest request) {
        Widget widgetToEdit = get(id);

        lock.writeLock().lock();
        widgetToEdit.setX(request.getX());
        widgetToEdit.setY(request.getY());
        widgetToEdit.setWidth(request.getWidth());
        widgetToEdit.setHeight(request.getHeight());
        widgetToEdit.setModifiedDate(LocalDateTime.now());

        if(request.getZ() != widgetToEdit.getZ()) {
            widgetToEdit.setZ(request.getZ());
            //remove it and insert with new z-index
            if(widgetsRepo.removeIf(it -> it.getId().equals(id))) {
                pushIntoStore(widgetToEdit, request.getZ() == null);
            } else {
                throw new WidgetNotFoundException(id);
            }
        }

        lock.writeLock().unlock();
        return widgetToEdit;
    }

    @Override
    public Boolean delete(UUID id) {
        lock.writeLock().lock();
        try {
            return widgetsRepo.removeIf(it -> it.getId().equals(id));
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public List<Widget> getAll() {
        return widgetsRepo;
    }


}
