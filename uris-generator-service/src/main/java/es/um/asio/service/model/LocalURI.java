package es.um.asio.service.model;

import es.um.asio.service.filter.CanonicalURIFilter;
import es.um.asio.service.filter.LocalURIFilter;
import es.um.asio.service.filter.SearchCriteria;
import es.um.asio.service.filter.SearchOperation;
import es.um.asio.service.util.ValidationConstants;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.SafeHtml;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = LocalURI.TABLE)
@Getter
@ToString(includeFieldNames = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class LocalURI {

    /**
     * Version ID.
     */
    private static final long serialVersionUID = -8605786237765754671L;

    /**
     * The id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = Columns.ID)
    @EqualsAndHashCode.Include
    @ApiModelProperty(hidden = true)
    private long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "canonical_URI_language_id", referencedColumnName = "id")
    private CanonicalURILanguage canonicalURILanguage;

    /**
     * Relation Bidirectional CanonicalURI ManyToOne
     */
    @EqualsAndHashCode.Include
    @ApiModelProperty(	example="ES",allowEmptyValue = false, position=3, readOnly=false, value = "Required: Storage Type", required = true)
    @ManyToOne(fetch = FetchType.LAZY)
    private StorageType storageType;

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
    @Column(name = CanonicalURILanguage.Columns.SUB_DOMAIN, nullable = true,columnDefinition = "VARCHAR(100)",length = 100)
    private String subDomain;

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
     * Table name.
     */
    public static final String TABLE = "LOCAL_URI";
    /**
     * Column name constants.
     */
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    static final class Columns {
        /**
         * TYPE ID column.
         */
        protected static final String ID = "ID";

        /**
         * TYPE CANONICAL_URI_LANGUAGE_ID column.
         */
        protected static final String CANONICAL_URI_LANGUAGE_ID = "CANONICAL_URI_LANGUAGE_ID";

        /**
         * TYPE STO STORAGETYPE_ID.
         */
        protected static final String STORAGETYPE_ID = "STORAGETYPE_ID";

        /**
         * NAME TYPE COLUMN.
         */
        protected static final String NAME = "NAME";

        /**
         * DOMAIN COLUMN.
         */
        protected static final String DOMAIN = "DOMAIN";

        /**
         * SUB DOMAIN COLUMN.
         */
        protected static final String SUB_DOMAIN = "SUB_DOMAIN";

        /**
         * TYPE COLUMN.
         */
        protected static final String TYPE = "TYPE";

        /**
         * CONCEPT COLUMN.
         */
        protected static final String CONCEPT = "CONCEPT";

        /**
         * REFERENCE COLUMN.
         */
        protected static final String REFERENCE = "REFERENCE";

        /**
         * REFERENCE COLUMN.
         */
        protected static final String FULL_URI = "FULL_URI";
    }

    public LocalURIFilter buildFilterByEntity() {
        LocalURIFilter f = new LocalURIFilter();
        if (this.id != 0) {
            f.add(new SearchCriteria(Columns.ID, this.id, SearchOperation.EQUAL));
        }
        if (this.canonicalURILanguage != null && this.canonicalURILanguage.getId()>0) {
            f.add(new SearchCriteria(Columns.CANONICAL_URI_LANGUAGE_ID, this.canonicalURILanguage.getId(), SearchOperation.EQUAL));
        }
        if (this.storageType != null && this.storageType.getId()>0) {
            f.add(new SearchCriteria(Columns.STORAGETYPE_ID, this.storageType.getId(), SearchOperation.EQUAL));
        }
        if (this.domain != null && !this.domain.equals("")) {
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

    public LocalURIFilter buildFilterByEntityUniqueProperties() {
        LocalURIFilter f = new LocalURIFilter();

        if (this.id != 0) {
            f.add(new SearchCriteria(Columns.ID, this.id, SearchOperation.EQUAL));
        } else {
            if (this.canonicalURILanguage != null && this.canonicalURILanguage.getId()>0) {
                f.add(new SearchCriteria(Columns.CANONICAL_URI_LANGUAGE_ID, this.canonicalURILanguage.getId(), SearchOperation.EQUAL));
            }
            if (this.storageType != null && this.storageType.getId()>0) {
                f.add(new SearchCriteria(Columns.STORAGETYPE_ID, this.storageType.getId(), SearchOperation.EQUAL));
            }
        }

        return f;
    }
}
