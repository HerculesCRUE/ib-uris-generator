package es.um.asio.service.model;

import es.um.asio.service.filter.*;
import es.um.asio.service.util.ValidationConstants;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.SafeHtml;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = LanguageType.TABLE)
@Getter
@ToString(includeFieldNames = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@IdClass(LanguageType.LanguageTypeId.class)
public class LanguageType implements Serializable {

    /**
     * Version ID.
     */
    private static final long serialVersionUID = -8605786237765754619L;

    /**
     * Relation Bidirectional Language ManyToOne
     */
    @Id
    @EqualsAndHashCode.Include
    @ApiModelProperty(	example="ES",allowEmptyValue = false, position=1, readOnly=false, value = "Required: ISO Code of Country", required = true)
    @ManyToOne(fetch = FetchType.LAZY)
    private Language language;


    /**
     * Relation Bidirectional Type ManyToOne
     */
    @Id
    @ApiModelProperty(	example="ES",allowEmptyValue = false, position=2, readOnly=false, value = "Required: Type", required = true)
    @ManyToOne(fetch = FetchType.LAZY)
    private Type type;

    /**
     * NAME.
     */
    @ApiModelProperty(	example="español",allowEmptyValue = false, position =3, readOnly=false, value = "Required: name of Type in language.", required = true)
    @Size(min = 1, max = ValidationConstants.MAX_LENGTH_DEFAULT)
    @SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
    @Column(name = Columns.NAME, nullable = false,columnDefinition = "VARCHAR(100)",length = 100)
    private String name;

    /*
    @OneToMany(mappedBy = "languageType", cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private Set<CanonicalURILanguage> canonicalURILanguages;
    */

    /**
     * Table name.
     */
    public static final String TABLE = "LANGUAGE_TYPE";

    /**
     * Column name constants.
     */
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    static final class Columns {
        /**
         * TYPE ID column.
         */
        protected static final String ISO = "LANGUAGE_ISO";

        /**
         * NAME TYPE COLUMN.
         */
        protected static final String TYPE = "TYPE_CODE";

        /**
         * TYPE ID column.
         */
        protected static final String NAME = "NAME";
    }

    public LanguageTypeFilter buildFilterByEntity() {
        LanguageTypeFilter f = new LanguageTypeFilter();
        if (this.language != null && this.language.getISO() != null) {
            f.add(new SearchCriteria(Columns.ISO, this.language.getISO(), SearchOperation.EQUAL));
        }
        if (this.type != null && this.type.getCode() != null && this.type.getCode() != "") {
            f.add(new SearchCriteria(Columns.TYPE, this.type.getCode() , SearchOperation.EQUAL));
        }
        if (this.name != null && !this.name.equals("")) {
            f.add(new SearchCriteria(Columns.NAME, this.name, SearchOperation.EQUAL));
        }
        return f;
    }

    public LanguageTypeFilter buildFilterByEntityUniqueProperties() {
        LanguageTypeFilter f = new LanguageTypeFilter();
        if ((this.language != null && this.language.getISO() != null) && (this.type != null && this.type.getCode() != null && this.type.getCode() != "") ) {
            f.add(new SearchCriteria(Columns.ISO, this.language.getISO(), SearchOperation.EQUAL));
            f.add(new SearchCriteria(Columns.TYPE, this.type.getCode() , SearchOperation.EQUAL));
        }
        return f;
    }

    public static class LanguageTypeId implements Serializable {
        private Language language;
        private Type type;

        public LanguageTypeId() {
        }

        public LanguageTypeId(Language language, Type type) {
            this.language = language;
            this.type = type;
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof LanguageType)) {
                return false;
            }
            LanguageType languageType = (LanguageType) o;
            return Objects.equals(language, languageType.getLanguage()) &&
                    Objects.equals(type, languageType.getType());
        }

        @Override
        public int hashCode() {
            return Objects.hash(language, type);
        }
    }

}
