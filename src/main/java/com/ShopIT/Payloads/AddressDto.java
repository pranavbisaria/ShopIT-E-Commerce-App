package com.ShopIT.Payloads;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddressDto {
    private Long Id;
    @NotNull
    private String type;
    @NotNull
    private String name;
    @NotNull
    @Pattern(regexp="^(\\+\\d{2}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$", message = "Not a valid number, please enter a 10 digit valid mobile number")
    private String mobile;
    @NotNull
    @Pattern(regexp="^[1-9][0-9]{5}$", message = "Not a valid pincode")
    private String pincode;
    private String locality;
    @NotBlank
    private String addressLine;
    @NotBlank
    private String city;
    @NotBlank
    private String state;
    private String landmark;
    @Pattern(regexp="^(\\+\\d{2}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$", message = "Not a valid number, please enter a 10 digit valid mobile number")
    private String mobile_alternative;
}
