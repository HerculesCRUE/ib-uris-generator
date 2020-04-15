package es.um.asio.service.model;

import es.um.asio.service.filter.*;
import es.um.asio.service.util.Utils;
import es.um.asio.service.util.ValidationConstants;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.SafeHtml;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;


@Entity
@Table(name = CanonicalURILanguage.TABLE)
@Getter
@ToString(includeFieldNames = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class CanonicalURILanguage implements Serializable {

    /**
     * Version ID.
     */
    private static final long serialVersionUID = -8605786237765754621L;

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
     * Relation Bidirectional CanonicalURI ManyToOne
     */
    @EqualsAndHashCode.Include
    @ApiModelProperty(	example="ES",allowEmptyValue = false, position=1, readOnly=false, value = "Required: Parent Canonical URI", required = true)
    @ManyToOne(fetch = FetchType.LAZY)
    private CanonicalURI canonicalURI;

    /**
     * Relation Bidirectional Language ManyToOne
     */
    @EqualsAndHashCode.Include
    @ApiModelProperty(	example="ES",allowEmptyValue = false, position=1, readOnly=false, value = "Required: Parent Canonical URI", required = true)
    @ManyToOne(fetch = FetchType.LAZY)
    private Language language;

    /**
     * DOMAIN.
     */
    @ApiModelProperty(	example="hercules",allowEmptyValue = false, position =1, readOnly=false, value = "Required: Domain represents the highest level of the namespace for URI resolution, and for providing relevant information about the owner of the information. ", required = true)
    @Size(min = 1, max = ValidationConstants.MAX_LENGTH_DEFAULT)
    @SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
    @Column(name = Columns.DOMAIN, nullable = false,columnDefinition = "VARCHAR(100)",length = 100)
    private String domain;

    /**
     * SUB_DOMAIN.
     */
    @ApiModelProperty(	example="um", allowEmptyValue = true,position =2, readOnly=false, value = "Optional:"+
            "It provides information about the entity or department within the entity to which the information resource belongs. Represents the lowest level of the namespace for URI resolution, and for providing relevant information about the owner of the information.", required = true)
    @Size(min = 1, max = ValidationConstants.MAX_LENGTH_DEFAULT)
    @SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
    @Column(name = Columns.SUB_DOMAIN, nullable = true,columnDefinition = "VARCHAR(100)",length = 100)
    private String subDomain;

    /**
     * TYPE.
     * Relation Bidirectional ManyToOne
     */
    /*
    @ApiModelProperty(	example="ref", allowEmptyValue = false, allowableValues = "cat, def, kos, ref",position =3, readOnly=false, value = "Required: " +
            "Sets the type of information the resource contains. One of this: cat | def | kos | res", required = true)
    @SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
    @ManyToOne(fetch = FetchType.LAZY)
    private LanguageType languageType;
    */
    @ApiModelProperty(	example="um", allowEmptyValue = true,position =3, readOnly=true, value = "Optional:"+
            "Type in Language.", required = true)
    @Size(min = 1, max = ValidationConstants.MAX_LENGTH_DEFAULT)
    @SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
    @Column(name = Columns.TYPE, nullable = false,columnDefinition = "VARCHAR(3)",length = 3)
    private String type;

    /**
     * CONCEPT.
     */
    @ApiModelProperty(	example="um", allowEmptyValue = true,position =4, readOnly=false, value = "Optional:"+
            "Concepts are abstract representations that correspond to the classes or properties of the vocabularies or ontologies used to semantically represent resources. In addition to the concept, an unambiguous reference to specific instances may be represented. Required only if is a ref type.", required = true)
    @Size(min = 1, max = ValidationConstants.MAX_LENGTH_DEFAULT)
    @SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
    @Column(name = Columns.CONCEPT, nullable = true,columnDefinition = "VARCHAR(100)",length = 100)
    private String concept;

    /**
     * REFERENCE.
     */
    @ApiModelProperty(	example="12345", allowEmptyValue = true,position =5, readOnly=false, value = "Optional:"+
            "Instances are representations in real world that correspond to the instances of the class defined in concepts. In addition to the concept, an unambiguous reference to specific instances may be represented. Required only if is a ref type, and concept is defined", required = true)
    @Size(min = 1, max = ValidationConstants.MAX_LENGTH_DEFAULT)
    @SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
    @Column(name = Columns.REFERENCE, nullable = true,columnDefinition = "VARCHAR(100)",length = 100)
    private String reference;

    /**
     * FULL_URI.
     */
    @ApiModelProperty(	example="12345", allowEmptyValue = true, position =6, readOnly=true, value = "Full URI Result", required = true)
    @Size(min = 1, max = ValidationConstants.MAX_LENGTH_DEFAULT)
    @SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
    @Column(name = Columns.FULL_URI, nullable = true,columnDefinition = "VARCHAR(400)",length = 400)
    private String fullURI;

    /**
     * TYPE.
     * Relation Bidirectional ManyToOne
     */
    @OneToOne(mappedBy = "canonicalURILanguage",fetch = FetchType.LAZY)
    private LocalURI localURI;

    /**
     * Table name.
     */
    public static final String TABLE = "CANONICAL_URI_LANGUAGE";

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
         * CANONICALURI_ID.
         */
        protected static final String CANONICALURI_ID = "CANONICALURI_ID";

        /**
         * ISO.
         */
        protected static final String ISO = "LANGUAGE_ISO";

        /**
         * DOMAIN NAME.
         */
        protected static final String DOMAIN = "DOMAIN";

        /**
         * SUB_DOMAIN NAME.
         */
        protected static final String SUB_DOMAIN = "SUB_DOMAIN";

        /**
         * TYPE NAME.
         */
        protected static final String TYPE = "TYPE";

        /**
         * CONCEPT column.
         */
        protected static final String CONCEPT = "CONCEPT";

        /**
         * REFERENCE column.
         */
        protected static final String REFERENCE = "REFERENCE";

        /**
         * URI column.
         */
        protected static final String FULL_URI = "FULL_URI";

    }

    public CanonicalURILanguageFilter buildFilterByEntity() {
        CanonicalURILanguageFilter f = new CanonicalURILanguageFilter();
        if (this.id != 0) {
            f.add(new SearchCriteria(Columns.ID, this.id, SearchOperation.EQUAL));
        }
        if (this.canonicalURI != null && this.canonicalURI.getId() > 0) {
            f.add(new SearchCriteria(Columns.CANONICALURI_ID, this.canonicalURI.getId(), SearchOperation.EQUAL));
        }
        if (this.language != null && this.language.getISO() != null) {
            f.add(new SearchCriteria(Columns.ISO, this.language.getISO(), SearchOperation.EQUAL));
        }
        if (this.domain != null) {
            f.add(new SearchCriteria(Columns.DOMAIN, this.domain, SearchOperation.EQUAL));
        }
        if (this.subDomain != null) {
            f.add(new SearchCriteria(Columns.SUB_DOMAIN, this.subDomain, SearchOperation.EQUAL));
        }
        if (this.type != null) {
            f.add(new SearchCriteria(Columns.TYPE, this.type, SearchOperation.EQUAL));
        }
        if (this.concept != null) {
            f.add(new SearchCriteria(Columns.CONCEPT, this.concept, SearchOperation.EQUAL));
        }
        if (this.reference != null) {
            f.add(new SearchCriteria(Columns.REFERENCE, this.reference, SearchOperation.EQUAL));
        }
        if (this.fullURI != null) {
            f.add(new SearchCriteria(Columns.FULL_URI, this.fullURI, SearchOperation.EQUAL));
        }
        return f;
    }

    public CanonicalURILanguageFilter buildFilterByEntityUniqueProperties() {
        CanonicalURILanguageFilter f = new CanonicalURILanguageFilter();

        if (this.id != 0) {
            f.add(new SearchCriteria(Columns.ID, this.id, SearchOperation.EQUAL));
        } else {
            if (this.canonicalURI != null && this.canonicalURI.getId() > 0) {
                f.add(new SearchCriteria(Columns.CANONICALURI_ID, this.canonicalURI.getId(), SearchOperation.EQUAL));
            }
            if (this.language != null && this.language.getISO() != null) {
                f.add(new SearchCriteria(Columns.ISO, this.language.getISO(), SearchOperation.EQUAL));
            }
            if (this.domain != null) {
                f.add(new SearchCriteria(Columns.DOMAIN, this.domain, SearchOperation.EQUAL));
            }
            if (this.subDomain != null) {
                f.add(new SearchCriteria(Columns.SUB_DOMAIN, this.subDomain, SearchOperation.EQUAL));
            }
            if (this.type != null) {
                f.add(new SearchCriteria(Columns.TYPE, this.type, SearchOperation.EQUAL));
            }
            if (this.concept != null) {
                f.add(new SearchCriteria(Columns.CONCEPT, this.concept, SearchOperation.EQUAL));
            }
            if (this.reference != null) {
                f.add(new SearchCriteria(Columns.REFERENCE, this.reference, SearchOperation.EQUAL));
            }
        }
        return f;
    }

    public void merge(CanonicalURILanguage other){
        if (other!=null && other.canonicalURI!=null && other.canonicalURI.getType()!=null)
            this.type = other.canonicalURI.getType().getCode();
        this.fullURI = other.fullURI;
    }

    public void generateFullURL(String uriSchema) {
        if (Utils.isValidString(this.domain)) {
            uriSchema = uriSchema.replaceFirst("$domain$",this.domain);
        } else {
            throw new IllegalArgumentException("Domain field in CanonicalURI can´t be empty");
        }
        if (Utils.isValidString(this.subDomain)) {
            uriSchema = uriSchema.replaceFirst("$sub-domain$",this.subDomain);
        } else {
            uriSchema = uriSchema.replaceFirst("$main$",this.subDomain);
        }
        if (this.language!=null && Utils.isValidString(this.language.getISO())) {
            uriSchema = uriSchema.replaceFirst("$language$",this.language.getISO());
        } else {
            uriSchema = uriSchema.replaceFirst("$main$",this.subDomain);
        }
        if (this.canonicalURI!=null && this.canonicalURI.getType()!=null && Utils.isValidString(this.canonicalURI.getType().getCode())) {
            this.type = this.canonicalURI.getType().getCode();
            uriSchema = uriSchema.replaceFirst("$type$",this.canonicalURI.getType().getCode());
        } else {
            throw new IllegalArgumentException("Type field in CanonicalURI can´t be empty");
        }
        if ( Utils.isValidString(this.concept)) {
            uriSchema = uriSchema.replaceFirst("$concept$",this.concept);
            if (Utils.isValidString(this.reference)) {
                uriSchema = uriSchema.replaceFirst("$reference$",this.reference);
            } else {
                if (canonicalURI!=null && (canonicalURI.getIsInstance() || canonicalURI.getIsProperty() ))
                    throw new IllegalArgumentException("Type field in CanonicalURI can´t be empty");
                else
                    uriSchema = uriSchema.replaceFirst("$reference$","");
            }
        } else {
            throw new IllegalArgumentException("Type field in CanonicalURI can´t be empty");
        }

        this.fullURI = uriSchema;
    }
}
