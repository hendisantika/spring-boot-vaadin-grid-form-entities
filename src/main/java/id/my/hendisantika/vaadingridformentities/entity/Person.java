package id.my.hendisantika.vaadingridformentities.entity;

import id.my.hendisantika.vaadingridformentities.entity.base.BaseEntity;
import id.my.hendisantika.vaadingridformentities.ui.FormField;
import id.my.hendisantika.vaadingridformentities.ui.GridColumn;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.Period;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-vaadin-grid-form-entities
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 20/11/24
 * Time: 09.07
 * To change this template use File | Settings | File Templates.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Person implements BaseEntity {
    @GridColumn(header = "ID", order = 0)
    private Long personId;

    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    @GridColumn(header = "First Name", order = 1)
    @FormField(label = "First Name", required = true, order = 1)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    @GridColumn(header = "Last Name", order = 2)
    @FormField(label = "Last Name", required = true, order = 2)
    private String lastName;

    @NotBlank(message = "Email is required")
    @GridColumn(header = "Email", order = 3, sortable = false)
    @FormField(label = "Email", required = true, order = 3)
    private String email;

    @GridColumn(header = "Is Active", order = 4)
    @FormField(label = "Is Active", required = true, order = 4)
    private Boolean isActive;

    @GridColumn(header = "Birthday", order = 6, dateTimeFormat = "dd.MM.yyyy")
    @FormField(label = "Birthday", required = true)
    private LocalDate birthday;

    @GridColumn(header = "Active Component", order = 5, showAsComponent = true)
    public Boolean exampleBoolean() {
        return isActive;  // Just for demonstration purpose Component
    }

    @GridColumn(header = "Age", order = 6)
    public int getAge() {
        if (birthday == null) return 0;
        return Period.between(birthday.atStartOfDay().toLocalDate(), LocalDate.now()).getYears();
    }
}
