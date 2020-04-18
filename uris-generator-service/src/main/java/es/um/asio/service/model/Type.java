package es.um.asio.service.model;

import es.um.asio.service.filter.CanonicalURIFilter;
import es.um.asio.service.filter.SearchCriteria;
import es.um.asio.service.filter.SearchOperation;
import es.um.asio.service.filter.TypeFilter;
import es.um.asio.service.util.ValidationConstants;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.SafeHtml;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity
@Table(name = Type.TABLE)
@Getter
@ToString(includeFieldNames = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Type {

    /**
     * Version ID.
     */
    private static final long serialVersionUID = -8605786237765754617L;

    /**
     * The id.
     */
    @Id
    @Column(name = Columns.CODE, columnDefinition = "VARCHAR(3)", length = 3)
    @EqualsAndHashCode.Include
    @ApiModelProperty(	example="cat",allowEmptyValue = false, position=1, readOnly=false, value = "Required: TYPE Code (3 characters)", required = true)
    private String code;

    /**
     * NAME.
     */
    @ApiModelProperty(	example="category",allowEmptyValue = true, position =2, readOnly=false, value = "Optional: Extended name of Type.", required = true)
    @Size(min = 1, max = ValidationConstants.MAX_LENGTH_DEFAULT)
    @SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
    @Column(name = Columns.NAME, nullable = false,columnDefinition = "VARCHAR(100)",length = 100)
    private String name;

    /**
     * Relation Bidirectional CanonicalURI OneToMany
     */
    @OneToMany(mappedBy = "type", cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private Set<CanonicalURI> canonicalURIs;
    /**
     * Relation Bidirectional LanguageType OneToMany
     */

    @OneToMany(mappedBy = "language", cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private Set<LanguageType> languageTypes;

    /**
     * Table name.
     */
    public static final String TABLE = "TYPE";

    public Type() {
    }

    public Type(String code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * Column name constants.
     */
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    static final class Columns {
        /**
         * TYPE ID column.
         */
        protected static final String CODE = "CODE";

        /**
         * NAME TYPE COLUMN.
         */
        protected static final String NAME = "NAME";
    }

    public TypeFilter buildFilterByEntity() {
        TypeFilter f = new TypeFilter();
        if (this.code != null && !this.code.equals("")) {
            f.add(new SearchCriteria("code", this.code, SearchOperation.EQUAL));
        }
        if (this.name != null && !this.name.equals("")) {
            f.add(new SearchCriteria("name", this.name, SearchOperation.EQUAL));
        }
        return f;
    }

    public TypeFilter buildFilterByEntityUniqueProperties() {
        TypeFilter f = new TypeFilter();
        if (this.code != null && !this.code.equals("")) {
            f.add(new SearchCriteria("code", this.code, SearchOperation.EQUAL));
        }
        return f;
    }
    public void merge(Type other){
        this.name = other.getName();
    }

}
