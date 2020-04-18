package es.um.asio.service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import es.um.asio.service.filter.*;
import es.um.asio.service.util.Utils;
import es.um.asio.service.util.ValidationConstants;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.SafeHtml;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

@Entity(name="language_type")
@Table(name = LanguageType.TABLE)
@Getter
@ToString(includeFieldNames = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class LanguageType implements Serializable {

    /**
     * Version ID.
     */
    private static final long serialVersionUID = -8605786237765754619L;

    /**
     * The id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = Columns.ID)
    @EqualsAndHashCode.Include
    @ApiModelProperty(hidden = true)
    private long id;

    /**
     * Relation Bidirectional Language ManyToOne
     */
    @JsonIgnore
    @ApiModelProperty(	example="ES",allowEmptyValue = false, position=1, readOnly=false, value = "Required: ISO Code of Country", required = true)
    @ManyToOne(fetch = FetchType.LAZY)
    private Language language;

    @JsonInclude
    @Transient
    private String languageId;

    /**
     * Relation Bidirectional Type ManyToOne
     */
    @JsonIgnore
    @ApiModelProperty(	example="ES",allowEmptyValue = false, position=2, readOnly=false, value = "Required: Type", required = true)
    @ManyToOne(fetch = FetchType.LAZY)
    private Type type;

    @JsonInclude
    @Transient
    private String typeId;

    /**
     * Code in Language.
     */
    @ApiModelProperty(	example="def",allowEmptyValue = false, position =3, readOnly=false, value = "Required: name of Type in language.", required = true)
    @Size(min = 1, max = ValidationConstants.MAX_LENGTH_DEFAULT)
    @SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
    @Column(name = Columns.TYPE_LANG_CODE, nullable = false,columnDefinition = "VARCHAR(3)",length = 3)
    private String typeLangCode;

    /**
     * DESCRIPTION in language.
     */
    @ApiModelProperty(	example="definición",allowEmptyValue = false, position =4, readOnly=false, value = "Required: name of Type in language.", required = true)
    @Size(min = 1, max = ValidationConstants.MAX_LENGTH_DEFAULT)
    @SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
    @Column(name = Columns.TYPE_LANG_DESCRIPTION, nullable = false,columnDefinition = "VARCHAR(100)",length = 100)
    private String description;

    /*
    @OneToMany(mappedBy = "languageType", cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private Set<CanonicalURILanguage> canonicalURILanguages;
    */

    /**
     * Table name.
     */
    public static final String TABLE = "LANGUAGE_TYPE";

    public LanguageType() {
    }

    public LanguageType(Language l, Type t, String typeLangCode, String description) {
        setLanguage(l);
        setType(t);
        this.typeLangCode = typeLangCode;
        this.description = description;
    }

    public void setLanguage(Language language) {
        this.language = language;
        if (this.language!=null) {
            this.languageId = language.getISO();
        }
    }

    public void setType(Type type) {
        this.type = type;
        if (this.type!=null) {
            this.typeId = type.getCode();
        }
    }

    public String getLanguageId() {
        if (languageId == null) {
            setLanguage(this.language);
        }
        return languageId;
    }

    public String getTypeId() {
        if (typeId == null) {
            setType(this.type);
        }
        return typeId;
    }

    /**
     * Column name constants.
     */
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    static final class Columns {

        /**
         * ID column.
         */
        protected static final String ID = "ID";

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
        protected static final String TYPE_LANG_CODE = "TYPE_LANG_CODE";

        /**
         * TYPE ID column.
         */
        protected static final String TYPE_LANG_DESCRIPTION = "TYPE_LANG_DESCRIPTION";
    }

    public LanguageTypeFilter buildFilterByEntity() {
        LanguageTypeFilter f = new LanguageTypeFilter();
        if (this.language != null && this.language.getISO() != null) {
            f.add(new SearchCriteria("language_iso", this.language.getISO(), SearchOperation.EQUAL));
        }
        if (this.type != null && this.type.getCode() != null && this.type.getCode() != "") {
            f.add(new SearchCriteria("type_code", this.type.getCode() , SearchOperation.EQUAL));
        }
        if (Utils.isValidString(this.typeLangCode)) {
            f.add(new SearchCriteria("typeLangCode", this.typeLangCode, SearchOperation.EQUAL));
        }
        if (Utils.isValidString(this.description)) {
            f.add(new SearchCriteria("description", this.description, SearchOperation.EQUAL));
        }
        return f;
    }

    public LanguageTypeFilter buildFilterByEntityUniqueProperties() {
        LanguageTypeFilter f = new LanguageTypeFilter();
        if (this.id >0 ) {
            f.add(new SearchCriteria("id", this.id, SearchOperation.EQUAL));
        } else {
            if (this.language != null && this.language.getISO() != null) {
                f.add(new SearchCriteria("language_iso", this.language.getISO(), SearchOperation.EQUAL));
            }
            if (this.type != null && this.type.getCode() != null) {
                f.add(new SearchCriteria("type_code", this.type.getCode(), SearchOperation.EQUAL));
            }
        }
        return f;
    }

    public void merge(LanguageType other){
        this.typeLangCode = other.getTypeLangCode();
        this.description = other.getDescription();
    }

    /*
    public static class LanguageTypeId implements Serializable {
        private String languageId;
        private String typeId;

        public LanguageTypeId() {
        }

        public LanguageTypeId(Language language, Type type) {
            this.languageId = (language!=null)?language.getISO():null;
            this.typeId = (type!=null)?type.getCode():null;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            LanguageTypeId that = (LanguageTypeId) o;
            return languageId.equals(that.languageId) &&
                    typeId.equals(that.typeId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(languageId, typeId);
        }
    }
     */

    @Override
    public String toString() {
        return "LanguageType{" +
                "id=" + id +
                ", languageId='" + languageId + '\'' +
                ", typeId='" + typeId + '\'' +
                ", typeLangCode='" + typeLangCode + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
