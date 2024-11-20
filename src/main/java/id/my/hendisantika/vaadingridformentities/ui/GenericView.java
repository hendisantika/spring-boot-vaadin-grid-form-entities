package id.my.hendisantika.vaadingridformentities.ui;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import id.my.hendisantika.vaadingridformentities.entity.base.BaseEntity;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-vaadin-grid-form-entities
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 20/11/24
 * Time: 09.09
 * To change this template use File | Settings | File Templates.
 */
public abstract class GenericView<T extends BaseEntity> extends VerticalLayout {
    private final Grid<T> grid;
    private final FormLayout form;
    private final Class<T> entityClass;
    private final Button save = new Button("Save");
    private final Button cancel = new Button("Cancel");
    private final Binder<T> binder;
    private final Div editor = new Div();
    private T selectedItem;

    protected GenericView(Class<T> entityClass) {
        this.entityClass = entityClass;
        this.grid = new Grid<>(entityClass, false);
        this.form = new FormLayout();
        this.binder = new Binder<>(entityClass);

        setupLayout();
        setupGrid();
        setupForm();
        setupEventHandlers();
    }

    private void setupLayout() {
        setSizeFull();

        // Create a split layout for grid and form
        SplitLayout splitLayout = new SplitLayout();
        splitLayout.setSizeFull();
        form.getStyle().set("margin-left", "20px");
        editor.add(form);
        editor.setVisible(false);

        splitLayout.addToPrimary(grid);
        splitLayout.addToSecondary(editor);

        splitLayout.setSplitterPosition(80.0);


        add(splitLayout);
    }

    private void setupGrid() {
        grid.setSizeFull();

        List<GridColumnInfo> gridColumns = new ArrayList<>();

        // Add field-based columns
        Arrays.stream(entityClass.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(GridColumn.class))
                .forEach(field -> {
                    GridColumn annotation = field.getAnnotation(GridColumn.class);
                    gridColumns.add(new GridColumnInfo(
                            field.getName(),
                            annotation.header().isEmpty() ? field.getName() : annotation.header(),
                            annotation.order(),
                            annotation.sortable(),
                            field.getType(),
                            null,
                            annotation.dateTimeFormat()
                    ));
                });

        // Add method-based columns
        Arrays.stream(entityClass.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(GridColumn.class))
                .forEach(method -> {
                    GridColumn annotation = method.getAnnotation(GridColumn.class);
                    String propertyName = method.getName();
                    if (propertyName.startsWith("get")) {
                        propertyName = propertyName.substring(3, 4).toLowerCase() +
                                propertyName.substring(4);
                    }

                    gridColumns.add(new GridColumnInfo(
                            propertyName,
                            annotation.header().isEmpty() ? propertyName : annotation.header(),
                            annotation.order(),
                            annotation.sortable(),
                            method.getReturnType(),
                            method,
                            annotation.dateTimeFormat()
                    ));
                });

        // Sort columns by order and add them to grid
        gridColumns.stream()
                .sorted(Comparator.comparingInt(GridColumnInfo::order))
                .forEach(columnInfo -> {
                    Grid.Column<T> column;

                    if (columnInfo.method() != null) {
                        // For method-based columns
                        column = grid.addColumn(item -> {
                            try {
                                Object value = columnInfo.method().invoke(item);
                                return formatValue(value, columnInfo);
                            } catch (Exception e) {
                                e.printStackTrace();
                                return null;
                            }
                        });
                    } else {
                        // For field-based columns
                        if (isTemporalType(columnInfo.type()) && !columnInfo.dateTimeFormat().isEmpty()) {
                            column = grid.addColumn(item -> {
                                try {
                                    Field field = entityClass.getDeclaredField(columnInfo.propertyName());
                                    field.setAccessible(true);
                                    Object value = field.get(item);
                                    return formatValue(value, columnInfo);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    return null;
                                }
                            });
                        } else {
                            column = grid.addColumn(columnInfo.propertyName());
                        }
                    }

                    column.setHeader(columnInfo.header())
                            .setSortable(columnInfo.sortable());
                });

        // Add selection listener
        grid.asSingleSelect().addValueChangeListener(event -> {
            selectedItem = event.getValue();
            editor.setVisible(true);
            binder.readBean(selectedItem);
        });
    }


    private boolean isTemporalType(Class<?> type) {
        return LocalDateTime.class.isAssignableFrom(type) ||
                LocalDate.class.isAssignableFrom(type) ||
                Date.class.isAssignableFrom(type);
    }

    private Object formatValue(Object value, GridColumnInfo columnInfo) {
        if (value == null) return "";

        if (!columnInfo.dateTimeFormat().isEmpty() && isTemporalType(columnInfo.type())) {
            switch (value) {
                case LocalDateTime localDateTime -> {
                    return DateTimeFormatter
                            .ofPattern(columnInfo.dateTimeFormat())
                            .format(localDateTime);
                }
                case LocalDate localDate -> {
                    return DateTimeFormatter
                            .ofPattern(columnInfo.dateTimeFormat())
                            .format(localDate);
                }
                case Date date -> {
                    return new SimpleDateFormat(columnInfo.dateTimeFormat())
                            .format(date);
                }
                default -> {
                }
            }
        }
        return value;
    }

    private void setupForm() {
        // Automatically add form fields based on FormField annotations
        Arrays.stream(entityClass.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(FormField.class))
                .sorted(Comparator.comparingInt(field ->
                        field.getAnnotation(FormField.class).order()))
                .forEach(field -> {
                    FormField annotation = field.getAnnotation(FormField.class);
                    String label = annotation.label().isEmpty() ?
                            field.getName() : annotation.label();

                    Component component = createFormField(field);
                    form.addFormItem(component, label);

                    setupFieldBinding(field, component);

                });

        // Add buttons
        HorizontalLayout buttons = new HorizontalLayout(save, cancel);
        form.add(buttons);
    }

    private void setupFieldBinding(Field field, Component component) {
        Class<?> type = field.getType();

        if (component instanceof NumberField numberField) {
            if (type == Integer.class || type == int.class) {
                binder.forField(numberField)
                        .withConverter(
                                // Convert Double to Integer when saving to bean
                                number -> number != null ? number.intValue() : null,
                                // Convert Integer to Double when reading from bean
                                integer -> integer != null ? integer.doubleValue() : null,
                                // Error message if conversion fails
                                "Please enter a valid number"
                        )
                        .bind(field.getName());
            } else if (type == Double.class || type == double.class) {
                binder.forField(numberField)
                        .bind(field.getName());
            } else if (type == Long.class || type == long.class) {
                binder.forField(numberField)
                        .withConverter(
                                number -> number != null ? number.longValue() : null,
                                longVal -> longVal != null ? longVal.doubleValue() : null,
                                "Please enter a valid number"
                        )
                        .bind(field.getName());
            }
        } else {
            // Handle other field types normally
            binder.forField((HasValue) component)
                    .bind(field.getName());
        }
    }

    private Component createFormField(Field field) {
        Class<?> type = field.getType();
        if (type == String.class) {
            return new TextField();
        } else if (type == Integer.class || type == int.class) {
            return new NumberField();
        } else if (type == Boolean.class || type == boolean.class) {
            return new Checkbox();
        } else if (type == LocalDateTime.class) {
            return new DateTimePicker();
        } else if (type == LocalDate.class) {
            return new DatePicker();
        }
        // Add more field types as needed
        return new TextField();
    }

    private void setupEventHandlers() {
        save.addClickListener(event -> {
            if (selectedItem != null && binder.writeBeanIfValid(selectedItem)) {
                saveEntity(selectedItem);
                refreshGrid();
                editor.setVisible(false);
            }
        });

        cancel.addClickListener(event -> {
            selectedItem = null;
            binder.readBean(null);
            editor.setVisible(false);
        });
    }

    // Abstract methods to be implemented by specific views
    protected abstract void saveEntity(T entity);

    protected abstract List<T> loadEntities();

    public void refreshGrid() {
        grid.setItems(loadEntities());
    }

    private record GridColumnInfo(String propertyName, String header, int order, boolean sortable, Class<?> type,
                                  Method method, String dateTimeFormat) {
    }

    public void addNew() {
        editor.setVisible(true);
        // Create empty instance
        selectedItem = createEmptyInstance();
        // Clear and set the binder
        binder.readBean(selectedItem);
        // Optional: Scroll form into view or highlight it
        form.getElement().scrollIntoView();
    }
}
