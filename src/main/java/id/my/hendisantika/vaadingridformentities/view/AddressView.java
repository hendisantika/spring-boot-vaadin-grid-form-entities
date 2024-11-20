package id.my.hendisantika.vaadingridformentities.view;

import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import id.my.hendisantika.vaadingridformentities.entity.Address;
import id.my.hendisantika.vaadingridformentities.ui.GenericView;

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
    }

}
