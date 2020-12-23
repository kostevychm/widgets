package me.kostevych.widgets.model;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.Objects;

/* Used for request */
@Valid
public class WidgetRequest {
    @NotNull
    private Integer x;
    @NotNull
    private Integer y;

    /* Could be empty in request */
    private Integer z;
    @Positive(message = "Width must be bigger than 0")
    private Integer width;
    @Positive(message = "Height must be bigger than 0")
    private Integer height;

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public Integer getZ() {
        return z;
    }

    public void setZ(Integer z) {
        this.z = z;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WidgetRequest that = (WidgetRequest) o;
        return x.equals(that.x) && y.equals(that.y) && Objects.equals(z, that.z) && width.equals(that.width) && height.equals(that.height);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z, width, height);
    }
}
