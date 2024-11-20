package id.my.hendisantika.vaadingridformentities;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Theme(value = "vaadin-entity", variant = Lumo.DARK)
public class SpringBootVaadinGridFormEntitiesApplication implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootVaadinGridFormEntitiesApplication.class, args);
    }

}
