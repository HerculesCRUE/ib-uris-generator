package es.um.asio.service.model;

import es.um.asio.service.filter.LanguageFilter;
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
@Table(name = Language.TABLE)
@Getter
@ToString(includeFieldNames = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Language {

    /**
     * Version ID.
     */
    private static final long serialVersionUID = -8605786237765754616L;

    /**
     * The id.
     */
    @Id
    @Column(name = Columns.ISO, columnDefinition = "VARCHAR(2)", length = 2)
    @EqualsAndHashCode.Include
    @ApiModelProperty(	example="ES",allowEmptyValue = false, position=1, readOnly=false, value = "Required: ISO Code of Country", required = false)
    private String ISO;

    /**
     * NAME.
     */
    @ApiModelProperty(	example="Spain",allowEmptyValue = false, position=2, readOnly=false, value = "Optional: Name of country. ", required = false)
    @Size(min = 1, max = ValidationConstants.MAX_LENGTH_DEFAULT)
    @SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
    @Column(name = Columns.NAME, nullable = true,columnDefinition = "VARCHAR(100)",length = 100)
    private String name;

    /**
     * DOMAIN NAME.
     */
    @ApiModelProperty(	example="dominio",allowEmptyValue = false, position=3, readOnly=false, value = "Required: Name of Domain in selected language. ", required = false)
    @Size(min = 1, max = ValidationConstants.MAX_LENGTH_DEFAULT)
    @SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
    @Column(name = Columns.DOMAIN, nullable = true,columnDefinition = "VARCHAR(100)",length = 100)
    private String domain;

    /**
     * SUB DOMAIN NAME.
     */
    @ApiModelProperty(	example="sub-dominio",allowEmptyValue = false, position=4, readOnly=false, value = "Required: Name of Sub Domain in selected language. ", required = false)
    @Size(min = 1, max = ValidationConstants.MAX_LENGTH_DEFAULT)
    @SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
    @Column(name = Columns.SUB_DOMAIN, nullable = true,columnDefinition = "VARCHAR(100)",length = 100)
    private String subDomain;

    /**
     * TYPE NAME.
     */
    @ApiModelProperty(	example="tipo",allowEmptyValue = false, position=4, readOnly=false, value = "Required: Name of type in selected language. ", required = false)
    @Size(min = 1, max = ValidationConstants.MAX_LENGTH_DEFAULT)
    @SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
    @Column(name = Columns.TYPE, nullable = true,columnDefinition = "VARCHAR(100)",length = 100)
    private String type;

    /**
     * CONCEPT NAME.
     */
    @ApiModelProperty(	example="class",allowEmptyValue = false, position=4, readOnly=false, value = "Required: Name of Concept(class) in selected language. ", required = false)
    @Size(min = 1, max = ValidationConstants.MAX_LENGTH_DEFAULT)
    @SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
    @Column(name = Columns.CONCEPT, nullable = true,columnDefinition = "VARCHAR(100)",length = 100)
    private String concept;

    /**
     * REFERENCE NAME.
     */
    @ApiModelProperty(	example="item",allowEmptyValue = false, position=4, readOnly=false, value = "Required: Name of Reference(item) in selected language. ", required = false)
    @Size(min = 1, max = ValidationConstants.MAX_LENGTH_DEFAULT)
    @SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
    @Column(name = Columns.REFERENCE, nullable = true,columnDefinition = "VARCHAR(100)",length = 100)
    private String reference;

    /**
     * Relation Bidirectional LanguageType OneToMany
     */
    @OneToMany(mappedBy = "language", cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private Set<LanguageType> languageTypes;

    /**
     * Relation Bidirectional CanonicalURILanguage OneToMany
     */
    @OneToMany(mappedBy = "language", cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private Set<CanonicalURILanguage> canonicalURILanguages;


    /**
     * Table name.
     */
    public static final String TABLE = "LANGUAGE";


    /**
     * Column name constants.
     */
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    static final class Columns {
        /**
         * ID column.
         */
        protected static final String ISO = "ISO";

        /**
         * ISO.
         */
        protected static final String NAME = "NAME";

        /**
         * DOMAIN NAME.
         */
        protected static final String DOMAIN = "DOMAIN_NAME";

        /**
         * SUB_DOMAIN NAME.
         */
        protected static final String SUB_DOMAIN = "SUB_DOMAIN_NAME";

        /**
         * TYPE NAME.
         */
        protected static final String TYPE = "TYPE_NAME";

        /**
         * CONCEPT NAME.
         */
        protected static final String CONCEPT = "CONCEPT_NAME";

        /**
         * URI column.
         */
        protected static final String REFERENCE = "REFERENCE_NAME";

    }

    public LanguageFilter buildFilterByEntity() {
        LanguageFilter f = new LanguageFilter();
        if (this.ISO != null && !this.ISO.equals("")) {
            f.add(new SearchCriteria(Columns.ISO, this.ISO, SearchOperation.EQUAL));
        }
        if (this.name != null && !this.name.equals("")) {
            f.add(new SearchCriteria(Columns.NAME, this.name, SearchOperation.EQUAL));
        }
        if (this.domain != null && !this.domain.equals("")) {
            f.add(new SearchCriteria(Columns.DOMAIN, this.domain, SearchOperation.EQUAL));
        }
        if (this.subDomain != null && !this.subDomain.equals("")) {
            f.add(new SearchCriteria(Columns.SUB_DOMAIN, this.subDomain, SearchOperation.EQUAL));
        }
        if (this.type != null && !this.type.equals("")) {
            f.add(new SearchCriteria(Columns.TYPE, this.type, SearchOperation.EQUAL));
        }
        if (this.concept != null && !this.concept.equals("")) {
            f.add(new SearchCriteria(Columns.CONCEPT, this.concept, SearchOperation.EQUAL));
        }
        if (this.reference != null && !this.reference.equals("")) {
            f.add(new SearchCriteria(Columns.REFERENCE, this.reference, SearchOperation.EQUAL));
        }
        return f;
    }

    public LanguageFilter buildFilterByEntityUniqueProperties() {
        LanguageFilter f = new LanguageFilter();
        if (this.ISO != null && !this.ISO.equals("")) {
            f.add(new SearchCriteria(Columns.ISO, this.ISO, SearchOperation.EQUAL));
        }
        return f;
    }

}
