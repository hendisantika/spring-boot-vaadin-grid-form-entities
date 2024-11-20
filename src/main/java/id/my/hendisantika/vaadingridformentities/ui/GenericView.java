package id.my.hendisantika.vaadingridformentities.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import id.my.hendisantika.vaadingridformentities.entity.base.BaseEntity;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
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

}
