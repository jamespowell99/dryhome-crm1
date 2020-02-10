package uk.co.dryhome.domain;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import uk.co.dryhome.service.docs.DocTemplate;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A ManualLabel.
 */
@Entity
@Table(name = "manual_label")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Slf4j
public class ManualLabel implements Serializable, MergeDocumentSource {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "line_1", nullable = false)
    private String line1;

    @Column(name = "line_2")
    private String line2;

    @Column(name = "line_3")
    private String line3;

    @Column(name = "line_4")
    private String line4;

    @Column(name = "line_5")
    private String line5;

    @Column(name = "line_6")
    private String line6;

    @Column(name = "line_7")
    private String line7;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLine1() {
        return line1;
    }

    public ManualLabel line1(String line1) {
        this.line1 = line1;
        return this;
    }

    public void setLine1(String line1) {
        this.line1 = line1;
    }

    public String getLine2() {
        return line2;
    }

    public ManualLabel line2(String line2) {
        this.line2 = line2;
        return this;
    }

    public void setLine2(String line2) {
        this.line2 = line2;
    }

    public String getLine3() {
        return line3;
    }

    public ManualLabel line3(String line3) {
        this.line3 = line3;
        return this;
    }

    public void setLine3(String line3) {
        this.line3 = line3;
    }

    public String getLine4() {
        return line4;
    }

    public ManualLabel line4(String line4) {
        this.line4 = line4;
        return this;
    }

    public void setLine4(String line4) {
        this.line4 = line4;
    }

    public String getLine5() {
        return line5;
    }

    public ManualLabel line5(String line5) {
        this.line5 = line5;
        return this;
    }

    public void setLine5(String line5) {
        this.line5 = line5;
    }

    public String getLine6() {
        return line6;
    }

    public ManualLabel line6(String line6) {
        this.line6 = line6;
        return this;
    }

    public void setLine6(String line6) {
        this.line6 = line6;
    }

    public String getLine7() {
        return line7;
    }

    public ManualLabel line7(String line7) {
        this.line7 = line7;
        return this;
    }

    public void setLine7(String line7) {
        this.line7 = line7;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ManualLabel manualLabel = (ManualLabel) o;
        if (manualLabel.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), manualLabel.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ManualLabel{" +
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

    @Override
    public Map<String, String> documentMappings() {
        HashMap<String, String> map = new HashMap<>();
        List<String> labelsContent = Stream.of(line1, line2, line3, line4, line5, line6, line7)
            .filter(x -> !StringUtils.isEmpty(x))
            .collect(Collectors.toList());

        for (int i = 0; i < 8; i++) {
            for (int n = 0; n < 7; n++) {

                String key = "label" + (i + 1) + "Line" + (n + 1);
                try {
                    map.put(key, labelsContent.get(n));
                } catch (IndexOutOfBoundsException e) {
                    map.put(key, "");
                }
                log.debug(key + ": " + map.get(key));
            }
        }

        return map;
    }

    @Override
    public String getMergeDocPrefix(DocTemplate docTemplate) {
        return getLine1() + "-" + docTemplate.getTemplateName();
    }
}
