package uk.co.dryhome.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the ManualLabel entity.
 */
public class ManualLabelDTO implements Serializable {

    private Long id;

    @NotNull
    private String line1;

    private String line2;

    private String line3;

    private String line4;

    private String line5;

    private String line6;

    private String line7;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLine1() {
        return line1;
    }

    public void setLine1(String line1) {
        this.line1 = line1;
    }

    public String getLine2() {
        return line2;
    }

    public void setLine2(String line2) {
        this.line2 = line2;
    }

    public String getLine3() {
        return line3;
    }

    public void setLine3(String line3) {
        this.line3 = line3;
    }

    public String getLine4() {
        return line4;
    }

    public void setLine4(String line4) {
        this.line4 = line4;
    }

    public String getLine5() {
        return line5;
    }

    public void setLine5(String line5) {
        this.line5 = line5;
    }

    public String getLine6() {
        return line6;
    }

    public void setLine6(String line6) {
        this.line6 = line6;
    }

    public String getLine7() {
        return line7;
    }

    public void setLine7(String line7) {
        this.line7 = line7;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ManualLabelDTO manualLabelDTO = (ManualLabelDTO) o;
        if (manualLabelDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), manualLabelDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ManualLabelDTO{" +
            "id=" + getId() +
            ", line1='" + getLine1() + "'" +
            ", line2='" + getLine2() + "'" +
            ", line3='" + getLine3() + "'" +
            ", line4='" + getLine4() + "'" +
            ", line5='" + getLine5() + "'" +
            ", line6='" + getLine6() + "'" +
            ", line7='" + getLine7() + "'" +
            "}";
    }
}
