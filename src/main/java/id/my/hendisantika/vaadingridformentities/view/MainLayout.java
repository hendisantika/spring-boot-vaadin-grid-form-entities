package id.my.hendisantika.vaadingridformentities.view;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.router.Layout;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-vaadin-grid-form-entities
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 20/11/24
 * Time: 09.15
 * To change this template use File | Settings | File Templates.
 */
@Layout
@AnonymousAllowed
@Slf4j
public class MainLayout extends AppLayout {

    private H1 viewTitle;

    public MainLayout() {
        setPrimarySection(Section.DRAWER);
        addDrawerContent();
        addHeaderContent();
    }
}
