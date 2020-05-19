package es.um.asio.service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import es.um.asio.service.filter.CanonicalURIFilter;
import es.um.asio.service.filter.LocalURIFilter;
import es.um.asio.service.filter.SearchCriteria;
import es.um.asio.service.filter.SearchOperation;
import es.um.asio.service.util.Utils;
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

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.DETACH)
    private CanonicalURILanguage canonicalURILanguage;

    /**
     * canonicalURILanguageStr.
     */
    @ApiModelProperty(hidden = true)
    @SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
    @Column(name = Columns.CANONICAL_URI_LANGUAGE_ID, nullable = true,columnDefinition = "VARCHAR(400)",length = 400)
    private String canonicalURILanguageStr;

    /**
     * Relation Bidirectional CanonicalURI ManyToOne
     */
    @JsonIgnore
    @EqualsAndHashCode.Include
    @ApiModelProperty(	example="ES",allowEmptyValue = false, position=2, readOnly=false, value = "Required: Storage Type", required = true)
    @ManyToOne(fetch = FetchType.LAZY)
    private StorageType storageType;

    /**
     * canonicalURILanguageStr.
     */
    @ApiModelProperty(hidden = true)
    @SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
    @Column(name = Columns.STORAGETYPE_NAME, nullable = true,columnDefinition = "VARCHAR(100)",length = 100)
    private String storageTypeStr;

    /**
     * localURI.
     */
    @ApiModelProperty(	example="12345", allowEmptyValue = true, position =3, readOnly=true, value = "Full URI Result", required = true)
    @Size(min = 1, max = ValidationConstants.MAX_LENGTH_DEFAULT)
    @SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
    @Column(name = Columns.LOCAL_URI, unique = false, nullable = true,columnDefinition = "VARCHAR(400)",length = 400)
    private String localURI;


    public LocalURI() {
    }

    public LocalURI(String localURI, CanonicalURILanguage cul, StorageType st) {
        this.localURI = localURI;
        this.canonicalURILanguage = cul;
        if (this.canonicalURILanguage!=null) {
            this.canonicalURILanguageStr = this.canonicalURILanguage.getFullURI();
        }
        this.storageType = st;
        if (this.storageType!=null) {
            this.storageTypeStr = this.storageType.getName();
        }
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setCanonicalURILanguage(CanonicalURILanguage canonicalURILanguage) {
        this.canonicalURILanguage = canonicalURILanguage;
    }

    public void setCanonicalURILanguageStr(String canonicalURILanguageStr) {
        this.canonicalURILanguageStr = canonicalURILanguageStr;
    }

    public void setStorageType(StorageType storageType) {
        this.storageType = storageType;
    }

    public void setStorageTypeStr(String storageTypeStr) {
        this.storageTypeStr = storageTypeStr;
    }

    public void setLocalURI(String localURI) {
        this.localURI = localURI;
    }

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
         * TYPE CANONICAL_URI_LANGUAGE_ID column.
         */
        protected static final String CANONICAL_URI_LANGUAGE = "CANONICAL_URI_LANGUAGE";

        /**
         * TYPE STO STORAGETYPE_ID.
         */
        protected static final String STORAGETYPE_NAME= "STORAGETYPE_NAME";

        /**
         * TYPE STO STORAGETYPE_ID.
         */
        protected static final String STORAGETYPE = "STORAGETYPE_ID";

        /**
         * REFERENCE COLUMN.
         */
        protected static final String LOCAL_URI = "LOCAL_URI";
    }

    public LocalURIFilter buildFilterByEntity() {
        LocalURIFilter f = new LocalURIFilter();
        if (this.id != 0) {
            f.add(new SearchCriteria("id", this.id, SearchOperation.EQUAL));
        } else {
            if (Utils.isValidString(this.localURI)) {
                f.add(new SearchCriteria("localURI", this.localURI, SearchOperation.EQUAL));
            }
            if (Utils.isValidString(this.canonicalURILanguageStr)) {
                f.add(new SearchCriteria("canonicalURILanguageStr", this.canonicalURILanguageStr, SearchOperation.EQUAL));
            }
            if (Utils.isValidString(this.storageTypeStr)) {
                f.add(new SearchCriteria("storageTypeStr", this.storageTypeStr, SearchOperation.EQUAL));
            }
        }
        return f;
    }

    public LocalURIFilter buildFilterByEntityUniqueProperties() {
        LocalURIFilter f = new LocalURIFilter();

        if (this.id != 0) {
            f.add(new SearchCriteria("id", this.id, SearchOperation.EQUAL));
        } else {
            if (Utils.isValidString(this.localURI)) {
                f.add(new SearchCriteria("localURI", this.localURI, SearchOperation.EQUAL));
            }
            if (Utils.isValidString(this.localURI)) {
                f.add(new SearchCriteria("canonicalURILanguageStr", this.canonicalURILanguageStr, SearchOperation.EQUAL));
            }
            if (Utils.isValidString(this.localURI)) {
                f.add(new SearchCriteria("storageTypeStr", this.storageTypeStr, SearchOperation.EQUAL));
            }
        }

        return f;
    }

    public void merge(LocalURI other){
        this.localURI = other.localURI;
    }

}
