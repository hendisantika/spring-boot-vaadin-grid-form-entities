package id.my.hendisantika.vaadingridformentities.entity;

import id.my.hendisantika.vaadingridformentities.entity.base.BaseEntity;
import id.my.hendisantika.vaadingridformentities.ui.FormField;
import id.my.hendisantika.vaadingridformentities.ui.GridColumn;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
public class Address implements BaseEntity {
    @GridColumn(header = "ID", order = 0)
    private Long addressId;

    @NotBlank(message = "Street is required")
    @Size(min = 1, max = 100, message = "Street must be between 1 and 100 characters")
    @GridColumn(header = "Street", order = 1)
    @FormField(label = "Street", required = true, order = 1)
    private String street;

    @NotBlank(message = "City is required")
    @Size(min = 1, max = 100, message = "City must be between 1 and 100 characters")
    @GridColumn(header = "City", order = 2)
    @FormField(label = "City", required = true, order = 2)
    private String city;

    @NotBlank(message = "Country is required")
    @Size(min = 1, max = 100, message = "Country must be between 1 and 100 characters")
    @GridColumn(header = "Country", order = 3)
    @FormField(label = "Country", required = true, order = 3)
    private String country;

    @GridColumn(header = "Postal Code", order = 4)
    @FormField(label = "Postal Code", required = true, order = 4)
    private String postalCode;

    @GridColumn(header = "Demo Value", order = 5, sortable = false)
    public String demoValue() {
        return "Hello World!";  // Just for demonstration purpose
    }
}
