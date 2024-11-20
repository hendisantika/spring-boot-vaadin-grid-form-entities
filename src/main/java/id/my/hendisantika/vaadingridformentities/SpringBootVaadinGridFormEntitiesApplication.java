package id.my.hendisantika.vaadingridformentities;

import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Theme(value = "vaadin-entity", variant = Lumo.DARK)
public class SpringBootVaadinGridFormEntitiesApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootVaadinGridFormEntitiesApplication.class, args);
    }

}
