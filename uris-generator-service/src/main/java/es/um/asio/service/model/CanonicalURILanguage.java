package es.um.asio.service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
     * TYPE.
     * Relation Bidirectional ManyToOne
     */
    @ApiModelProperty(hidden = true)
    @Column(name = Columns.LANGUAGE_ID, nullable = true,columnDefinition = "VARCHAR(20)",length = 20)
    private String languageID;


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

    @JsonIgnore
    @Transient
    private LanguageType languageType;

    @ApiModelProperty(	example="um", allowEmptyValue = true,position =3, readOnly=true, value = "Optional:"+
            "Type in Language.", required = true)
    @Size(min = 1, max = ValidationConstants.MAX_LENGTH_DEFAULT)
    @SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
    @Column(name = Columns.TYPE, nullable = false,columnDefinition = "VARCHAR(3)",length = 3)
    private String typeCode;

    @ApiModelProperty(	example="um", allowEmptyValue = true,position =3, readOnly=true, value = "Optional:"+
            "Type in Language.", required = true)
    @Size(min = 1, max = ValidationConstants.MAX_LENGTH_DEFAULT)
    @SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
    @Column(name = Columns.TYPE_LANGUAGE, nullable = false,columnDefinition = "VARCHAR(3)",length = 3)
    private String typeLangCode;

    @JsonIgnore
    @Transient
    private Type type;

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
     * Entity Name.
     */
    @ApiModelProperty(	value = "name of entity", example="entity", allowEmptyValue = false,position =10, readOnly=false, required = true)
    @Column(name = Columns.ENTITY_NAME, nullable = false,columnDefinition = "VARCHAR(100)",length = 100)
    private String entityName;

    /**
     * Property Name.
     */
    @ApiModelProperty(	value = "name of property", example="property", allowEmptyValue = true,position =11, readOnly=false, required = false)
    @Column(name = Columns.PROPERTY_NAME, nullable = true,columnDefinition = "VARCHAR(100)",length = 100)
    private String propertyName;

    /**
     * Entity Name.
     */
    @ApiModelProperty(	value = "name of parent entity", example="entity", allowEmptyValue = false,position =10, readOnly=false, required = true)
    @Column(name = Columns.PARENT_ENTITY_NAME, nullable = false,columnDefinition = "VARCHAR(100)",length = 100)
    private String parentEntityName;

    /**
     * Property Name.
     */
    @ApiModelProperty(	value = "name of property", example="property", allowEmptyValue = true,position =11, readOnly=false, required = false)
    @Column(name = Columns.PARENT_PROPERTY_NAME, nullable = true,columnDefinition = "VARCHAR(100)",length = 100)
    private String parentPropertyName;

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

    public CanonicalURILanguage() {
    }

    public CanonicalURILanguage(CanonicalURI canonicalURI,String domain, String subDomain, Type t,LanguageType lt, String concept, String reference) {
        this.canonicalURI = canonicalURI;
        this.domain = domain;
        this.subDomain = subDomain;
        this.typeCode = t.getCode();
        this.type = t;
        this.typeLangCode = lt.getTypeLangCode();
        this.languageType = lt;
        setConcept(concept);
        setReference(reference);
        generateFullURL();
    }

    public void setConcept(String concept) {
        this.concept = concept;
        this.entityName = concept;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
        this.reference = propertyName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
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
         * CANONICALURI_ID.
         */
        protected static final String CANONICALURI_ID = "CANONICALURI_ID";

        /**
         * ISO.
         */
        protected static final String ISO = "LANGUAGE_ISO";

        /**
         * ISO.
         */
        protected static final String LANGUAGE_ID = "LANGUAGE_ID";

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
         * TYPE_LANGUAGE NAME.
         */
        protected static final String TYPE_LANGUAGE = "TYPE_LANGUAGE";

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

        /**
         * ENTITY_NAME column.
         */
        protected static final String ENTITY_NAME = "ENTITY_NAME";

        /**
         * PROPERTY_NAME column.
         */
        protected static final String PROPERTY_NAME = "PROPERTY_NAME";

        /**
         * ENTITY_NAME column.
         */
        protected static final String PARENT_ENTITY_NAME = "PARENT_ENTITY_NAME";

        /**
         * PROPERTY_NAME column.
         */
        protected static final String PARENT_PROPERTY_NAME = "PARENT_PROPERTY_NAME";
    }

    public CanonicalURILanguageFilter buildFilterByEntity() {
        CanonicalURILanguageFilter f = new CanonicalURILanguageFilter();
        if (this.id != 0) {
            f.add(new SearchCriteria(Columns.ID, this.id, SearchOperation.EQUAL));
        }
        if (this.canonicalURI != null && this.canonicalURI.getId() > 0) {
            f.add(new SearchCriteria("", this.canonicalURI.getId(), SearchOperation.EQUAL));
        }
        if (this.language != null && this.language.getISO() != null) {
            f.add(new SearchCriteria("language", this.language.getISO(), SearchOperation.EQUAL));
        }
        if (this.domain != null) {
            f.add(new SearchCriteria("domain", this.domain, SearchOperation.EQUAL));
        }
        if (this.subDomain != null) {
            f.add(new SearchCriteria("subDomain", this.subDomain, SearchOperation.EQUAL));
        }
        if (this.type != null) {
            f.add(new SearchCriteria("typeCode", this.typeCode, SearchOperation.EQUAL));
        }
        if (this.type != null) {
            f.add(new SearchCriteria("typeLangCode", this.typeLangCode, SearchOperation.EQUAL));
        }
        if (this.concept != null) {
            f.add(new SearchCriteria("concept", this.concept, SearchOperation.EQUAL));
        }
        if (this.reference != null) {
            f.add(new SearchCriteria("reference", this.reference, SearchOperation.EQUAL));
        }
        if (this.fullURI != null) {
            f.add(new SearchCriteria("fullURI", this.fullURI, SearchOperation.EQUAL));
        }
        if (this.entityName != null) {
            f.add(new SearchCriteria("entityName", this.entityName, SearchOperation.EQUAL));
        }
        if (this.propertyName != null) {
            f.add(new SearchCriteria("propertyName", this.propertyName, SearchOperation.EQUAL));
        }
        if (this.parentEntityName != null) {
            f.add(new SearchCriteria("parentEntityName", this.parentEntityName, SearchOperation.EQUAL));
        }
        if (this.parentPropertyName != null) {
            f.add(new SearchCriteria("parentPropertyName", this.parentPropertyName, SearchOperation.EQUAL));
        }
        return f;
    }

    public CanonicalURILanguageFilter buildFilterByEntityUniqueProperties() {
        CanonicalURILanguageFilter f = new CanonicalURILanguageFilter();

        if (this.id != 0) {
            f.add(new SearchCriteria("id", this.id, SearchOperation.EQUAL));
        } else {
            if (this.language != null && this.language.getISO() != null) {
                f.add(new SearchCriteria("languageID", this.languageID, SearchOperation.EQUAL));
            }
            if (this.domain != null) {
                f.add(new SearchCriteria("domain", this.domain, SearchOperation.EQUAL));
            }
            if (this.subDomain != null) {
                f.add(new SearchCriteria("subDomain", this.subDomain, SearchOperation.EQUAL));
            }
            if (this.type != null) {
                f.add(new SearchCriteria("typeLangCode", this.typeLangCode, SearchOperation.EQUAL));
            }
            if (this.concept != null) {
                f.add(new SearchCriteria("concept", this.concept, SearchOperation.EQUAL));
            }
            if (this.reference != null) {
                f.add(new SearchCriteria("reference", this.reference, SearchOperation.EQUAL));
            }
            if (this.entityName != null) {
                f.add(new SearchCriteria("entityName", this.entityName, SearchOperation.EQUAL));
            }
            if (this.propertyName != null) {
                f.add(new SearchCriteria("propertyName", this.propertyName, SearchOperation.EQUAL));
            }
            if (this.parentEntityName != null) {
                f.add(new SearchCriteria("parentEntityName", this.parentEntityName, SearchOperation.EQUAL));
            }
            if (this.parentPropertyName != null) {
                f.add(new SearchCriteria("parentPropertyName", this.parentPropertyName, SearchOperation.EQUAL));
            }
        }
        return f;
    }

    public void merge(CanonicalURILanguage other){
        if (other!=null && other.fullURI!=null)
            this.typeCode = other.fullURI;
        if (other!=null && other.typeLangCode!=null)
            this.typeLangCode = other.typeLangCode;
    }

    public void generateFullURL() {
        generateFullURL("http://$domain$/$sub-domain$/$language$/$type$/$concept$/$reference$");
    }

    public void generateFullURL(String uriSchema) {
        if (Utils.isValidString(this.domain)) {
            uriSchema = uriSchema.replaceFirst("\\$domain\\$",this.domain);
        } else {
            throw new IllegalArgumentException("Domain field in CanonicalLanguageURI can´t be empty");
        }
        if (Utils.isValidString(this.subDomain)) {
            uriSchema = uriSchema.replaceFirst("\\$sub-domain\\$",this.subDomain);
        } else {
            uriSchema = uriSchema.replaceFirst("/\\$sub-domain\\$","");
        }
        if (Utils.isValidString(this.languageID)) {
            uriSchema = uriSchema.replaceFirst("\\$language\\$",this.languageID);
        } else {
            throw new IllegalArgumentException("languageID field in CanonicalLanguageURI can´t be empty");
        }
        if (Utils.isValidString(this.type.getCode())) {
            uriSchema = uriSchema.replaceFirst("\\$type\\$",this.typeLangCode);
        } else {
            throw new IllegalArgumentException("Type field in CanonicalLanguageURI can´t be empty");
        }
        if ( Utils.isValidString(this.concept)) {
            uriSchema = uriSchema.replaceFirst("\\$concept\\$",this.concept);
            if (Utils.isValidString(this.reference)) {
                uriSchema = uriSchema.replaceFirst("\\$reference\\$",this.reference);
            } else {
                uriSchema = uriSchema.replaceFirst("\\$reference\\$","");
            }
        } else {
            throw new IllegalArgumentException("Concept field in CanonicalLanguageURI can´t be empty");
        }

        this.fullURI = uriSchema;
    }

}
