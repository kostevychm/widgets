package me.kostevych.widgets.services;

import me.kostevych.widgets.model.Widget;
import me.kostevych.widgets.model.WidgetRequest;

import java.util.List;
import java.util.UUID;

public interface WidgetStoreService {
    /**
     * @param request WidgetRequest
     * @return added widget
     */
    Widget add(WidgetRequest request);

    /**
     *
     * @param id widget id
     * @return widget from repo
     */
    Widget get(UUID id);

    /**
     *
     * @param id id of widget you want to edit
     * @param request WidgetRequest
     * @return edited widget
     */
    Widget edit(UUID id, WidgetRequest request);

    /**
     *
     * @param id id of widget you want to delete
     * @return true when was deleted
     */
    Boolean delete(UUID id);

    /**
     *
     * @return all stored widgets
     */
    List<Widget> getAll();

}
