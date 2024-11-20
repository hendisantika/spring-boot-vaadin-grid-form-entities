package id.my.hendisantika.vaadingridformentities.view;

import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import id.my.hendisantika.vaadingridformentities.entity.Address;
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
 * Time: 09.14
 * To change this template use File | Settings | File Templates.
 */
@PageTitle("ADDRESSES")
@Menu(order = 1, icon = "line-awesome/svg/boxes-solid.svg")
@Route(value = "addresses", layout = MainLayout.class)
public class AddressView extends GenericView<Address> {

    private final DataService dataService;

    public AddressView(DataService dataService) {
        super(Address.class);
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
    protected void saveEntity(Address entity) {
        dataService.saveAddress(entity);
    }

    @Override
    protected List<Address> loadEntities() {
        return dataService.findAllAddresses();
    }

    @Override
    protected void deleteEntity(Address address) {
        dataService.deleteAddress(address);
    }
}
