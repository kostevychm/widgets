package me.kostevych.widgets.model;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/* Used for storing, we can use primitives here in order to save some memory as any value couldn't be null */
public class Widget {
    private UUID id;
    private LocalDateTime modifiedDate;

    private int x;
    private int y;
    private int z;
    private int width;
    private int height;

    public Widget(WidgetRequest request) {
        this.x = request.getX();
        this.y = request.getY();
        this.z = request.getZ();
        this.width = request.getWidth();
        this.height = request.getHeight();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(LocalDateTime modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public void incrementZ() {
       z++;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Widget widget = (Widget) o;
        return x == widget.x && y == widget.y && z == widget.z && width == widget.width && height == widget.height && id.equals(widget.id) && Objects.equals(modifiedDate, widget.modifiedDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, modifiedDate, x, y, z, width, height);
    }

    @Override
    public String toString() {
        return "Widget{" +
                "id=" + id +
                ", modifiedDate=" + modifiedDate +
                ", x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", width=" + width +
                ", height=" + height +
                '}';
    }
}
