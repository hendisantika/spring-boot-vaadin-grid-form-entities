package id.my.hendisantika.vaadingridformentities.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import id.my.hendisantika.vaadingridformentities.entity.base.BaseEntity;

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
}
