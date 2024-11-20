package id.my.hendisantika.vaadingridformentities.view;

import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import id.my.hendisantika.vaadingridformentities.entity.Person;
import id.my.hendisantika.vaadingridformentities.service.DataService;
import id.my.hendisantika.vaadingridformentities.ui.GenericView;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-vaadin-grid-form-entities
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 20/11/24
 * Time: 09.16
 * To change this template use File | Settings | File Templates.
 */
@PageTitle("PERSONS")
@Menu(order = 0, icon = "line-awesome/svg/home-solid.svg")
@Route(value = "", layout = MainLayout.class)
public class PersonView extends GenericView<Person> {

    private final DataService dataService;


    public PersonView(DataService dataService) {
        super(Person.class);
        this.dataService = dataService;
        refreshGrid();

        // Add Button for adding new person
        Div addButton = new Div();
        addButton.setClassName("circle-button-container");
        Avatar addAvatar = new Avatar("+");
        addAvatar.addClassName("circle-button");
        addButton.add(addAvatar);
        addButton.addClickListener(event -> addNew());
        gridContainer.addComponentAsFirst(addButton);
    }

    @Override
    protected void saveEntity(Person entity) {
    }

    @Override
    protected List<Person> loadEntities() {
        return dataService.findAllPersons();
    }

    @Override
    protected void deleteEntity(Person person) {
        dataService.deletePerson(person);
    }
}
