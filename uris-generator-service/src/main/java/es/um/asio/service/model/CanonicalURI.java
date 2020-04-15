package es.um.asio.service.model;

import es.um.asio.service.filter.CanonicalURIFilter;
import es.um.asio.service.filter.SearchCriteria;
import es.um.asio.service.filter.SearchOperation;
import es.um.asio.service.filter.URIMapFilter;
import es.um.asio.service.util.Utils;
import es.um.asio.service.util.ValidationConstants;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.SafeHtml;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = CanonicalURI.TABLE)
@Getter
@ToString(includeFieldNames = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class CanonicalURI implements Serializable {

    /**
     * Version ID.
     */
    private static final long serialVersionUID = -8605786237765754616L;

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
    @ApiModelProperty(	example="um", allowEmptyValue = true,position =2, readOnly=false, value = "Optional: \n" +
            "It provides information about the entity or department within the entity to which the information resource belongs. Represents the lowest level of the namespace for URI resolution, and for providing relevant information about the owner of the information.", required = true)
    @Size(min = 1, max = ValidationConstants.MAX_LENGTH_DEFAULT)
    @SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
    @Column(name = Columns.SUB_DOMAIN, nullable = true,columnDefinition = "VARCHAR(100)",length = 100)
    private String subDomain;

    /**
     * TYPE.
     * Relation Bidirectional ManyToOne
     */
    @ApiModelProperty(	example="ref", allowEmptyValue = false, allowableValues = "cat, def, kos, ref",position =3, readOnly=false, value = "Required: " +
            "Sets the type of information the resource contains. One of this: cat | def | kos | res", required = true)
    @SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
    @ManyToOne(fetch = FetchType.LAZY)
    private Type type;
    /**
     * CONCEPT.
     */
    @ApiModelProperty(	example="university", allowEmptyValue = true, position =4, readOnly=false, value = "Optional:"+
            "Concepts are abstract representations that correspond to the classes or properties of the vocabularies or ontologies used to semantically represent resources. In addition to the concept, an unambiguous reference to specific instances may be represented. Required only if is a ref type", required = true)
    @Size(min = 1, max = ValidationConstants.MAX_LENGTH_DEFAULT)
    @SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
    @Column(name = Columns.CONCEPT, nullable = true,columnDefinition = "VARCHAR(100)",length = 100)
    private String concept;

    /**
     * REFERENCE.
     */
    @ApiModelProperty(	example="12345", allowEmptyValue = true, position =5, readOnly=false, value = "Optional:"+
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
     * Is Entity.
     */
    @ApiModelProperty(	value = "true if is entity", example="true", allowEmptyValue = false, allowableValues = "true, false",position =7, readOnly=false, required = true)
    @Column(name = URIMap.Columns.IS_ENTITY)
    private Boolean isEntity = false;

    /**
     * Is Property.
     */
    @ApiModelProperty(	value = "true if is property", example="true", allowEmptyValue = false, allowableValues = "true, false",position =8, readOnly=false, required = true)
    @Column(name = URIMap.Columns.IS_PROPERTY)
    private Boolean isProperty = false;

    /**
     * Is Instance.
     */
    @ApiModelProperty(	value = "true if is instance", example="true", allowEmptyValue = false, allowableValues = "true, false",position =9, readOnly=false, required = true)
    @Column(name = URIMap.Columns.IS_INSTANCE)
    private Boolean isInstance = false;

    /**
     * Relation Bidirectional CanonicalURILanguage OneToMany
     */
    @OneToMany(mappedBy = "canonicalURI" , cascade = CascadeType.ALL)
    private Set<CanonicalURILanguage> canonicalURILanguages;

    /**
     * Table name.
     */
    public static final String TABLE = "CANONICAL_URI";

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
         * BASE URL column.
         */
        protected static final String DOMAIN = "DOMAIN";

        /**
         * BASE URL column.
         */
        protected static final String SUB_DOMAIN = "SUB_DOMAIN";

        /**
         * Entity column.
         */
        protected static final String TYPE = "TYPE_CODE";

        /**
         * Property column.
         */
        protected static final String CONCEPT = "CONCEPT";

        /**
         * URI column.
         */
        protected static final String REFERENCE = "REFERENCE";

        /**
         * IDENTIFIER column.
         */
        protected static final String FULL_URI = "FULL_URI";

        /**
         * IS_ENTITY column.
         */
        protected static final String IS_ENTITY = "IS_ENTITY";

        /**
         * IS_PROPERTY column.
         */
        protected static final String IS_PROPERTY = "IS_PROPERTY";

        /**
         * IS_Instance column.
         */
        protected static final String IS_INSTANCE = "IS_INSTANCE";

    }

    public CanonicalURIFilter buildFilterByEntity() {
        CanonicalURIFilter f = new CanonicalURIFilter();
        if (this.id != 0) {
            f.add(new SearchCriteria(Columns.ID, this.id, SearchOperation.EQUAL));
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
        if (this.isEntity != null) {
            f.add(new SearchCriteria(Columns.IS_ENTITY, this.isEntity, SearchOperation.EQUAL));
        }
        if (this.isProperty != null) {
            f.add(new SearchCriteria(Columns.IS_PROPERTY, this.isProperty, SearchOperation.EQUAL));
        }
        if (this.isInstance != null) {
            f.add(new SearchCriteria(Columns.IS_INSTANCE, this.isInstance, SearchOperation.EQUAL));
        }
        return f;
    }

    public CanonicalURIFilter buildFilterByEntityUniqueProperties() {
        CanonicalURIFilter f = new CanonicalURIFilter();

        if (this.id != 0) {
            f.add(new SearchCriteria(Columns.ID, this.id, SearchOperation.EQUAL));
        } else {
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

    public void merge(CanonicalURI other){
        this.isEntity = other.isEntity;
        this.isProperty = other.isProperty;
        this.isInstance = other.isInstance;
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
        if (Utils.isValidString(this.type.getCode())) {
            uriSchema = uriSchema.replaceFirst("$type$",this.type.getCode());
        } else {
            throw new IllegalArgumentException("Type field in CanonicalURI can´t be empty");
        }
        if ( Utils.isValidString(this.concept)) {
            uriSchema = uriSchema.replaceFirst("$concept$",this.concept);
            if (Utils.isValidString(this.reference)) {
                uriSchema = uriSchema.replaceFirst("$reference$",this.reference);
            } else {
                if (isInstance || isProperty)
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
