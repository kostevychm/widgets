package me.kostevych.widgets.controllers;

import me.kostevych.widgets.exceptions.ValidationError;
import me.kostevych.widgets.exceptions.ValidationErrorBuilder;
import me.kostevych.widgets.exceptions.WidgetNotFoundException;
import me.kostevych.widgets.model.Widget;
import me.kostevych.widgets.model.WidgetRequest;
import me.kostevych.widgets.services.WidgetStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Validated
@RestController
public class WidgetController {

    @Autowired
    private final WidgetStoreService widgetStore;

    public WidgetController(WidgetStoreService widgetStore) {
        this.widgetStore = widgetStore;
    }

    @GetMapping("/widgets")
    CollectionModel<EntityModel<Widget>> all() {
        List<EntityModel<Widget>> widgets = widgetStore.getAll().stream()
                .map(widget -> EntityModel.of(widget,
                        linkTo(methodOn(WidgetController.class).one(widget.getId())).withSelfRel(),
                        linkTo(methodOn(WidgetController.class).all()).withRel("widgets")))
                .collect(Collectors.toList());

        return CollectionModel.of(widgets, linkTo(methodOn(WidgetController.class).all()).withSelfRel());
    }

    @PostMapping("/widgets")
    public ResponseEntity<?> newWidget(@Valid @RequestBody WidgetRequest newWidget) {
        Widget addedWidget = widgetStore.add(newWidget);

        EntityModel<Widget> entityModel = EntityModel.of(addedWidget,
                linkTo(methodOn(WidgetController.class).one(addedWidget.getId())).withSelfRel(),
                linkTo(methodOn(WidgetController.class).all()).withRel("widgets"));

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()) //
                .body(entityModel);
    }

    @GetMapping("/widgets/{id}")
    EntityModel<Widget> one(@PathVariable UUID id) {

        return EntityModel.of(widgetStore.get(id),
                linkTo(methodOn(WidgetController.class).one(id)).withSelfRel(),
                linkTo(methodOn(WidgetController.class).all()).withRel("widgets"));
    }

    @PutMapping("/widgets/{id}")
    ResponseEntity<?>  replaceWidget(@Valid @RequestBody WidgetRequest newWidget, @PathVariable UUID id) {
        Widget editedWidget = widgetStore.edit(id, newWidget);

        EntityModel<Widget> entityModel = EntityModel.of(editedWidget,
                linkTo(methodOn(WidgetController.class).one(id)).withSelfRel(),
                linkTo(methodOn(WidgetController.class).all()).withRel("widgets"));

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @DeleteMapping("/widgets/{id}")
    ResponseEntity<?> deleteWidget(@PathVariable UUID id) {
        widgetStore.delete(id);
        return ResponseEntity.noContent().build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        return ResponseEntity.badRequest().body(ValidationErrorBuilder.fromBindingErrors(ex.getBindingResult().getAllErrors()));
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(WidgetNotFoundException.class)
    public ResponseEntity<?> handleNotFoundExceptions(
            WidgetNotFoundException ex) {
        ValidationError error = new ValidationError(ex.getMessage());
        return ResponseEntity.badRequest().body(error);
    }

}
