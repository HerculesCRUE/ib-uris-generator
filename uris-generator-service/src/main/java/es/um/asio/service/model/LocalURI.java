package es.um.asio.service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import es.um.asio.service.filter.LocalURIFilter;
import es.um.asio.service.filter.SearchCriteria;
import es.um.asio.service.filter.SearchOperation;
import es.um.asio.service.util.Utils;
import es.um.asio.service.util.ValidationConstants;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;

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
    @ManyToOne(cascade = CascadeType.DETACH,fetch = FetchType.LAZY)
    private CanonicalURILanguage canonicalURILanguage;

    /**
     * canonicalURILanguageStr.
     */
    @ApiModelProperty(hidden = true)
    @Column(name = Columns.CANONICAL_URI_LANGUAGE_ID, nullable = true,columnDefinition = "VARCHAR(400)",length = 400)
    private String canonicalURILanguageStr;

    /**
     * Relation Bidirectional CanonicalURI ManyToOne
     */
    @JsonIgnore
    @EqualsAndHashCode.Include
    @ApiModelProperty(	example="ES",allowEmptyValue = false, position=2, accessMode = ApiModelProperty.AccessMode.READ_WRITE, value = "Required: Storage Type", required = true)
    @ManyToOne(fetch = FetchType.LAZY)
    private StorageType storageType;

    /**
     * canonicalURILanguageStr.
     */
    @ApiModelProperty(hidden = true)
    @Column(name = Columns.STORAGETYPE_NAME, nullable = true,columnDefinition = "VARCHAR(100)",length = 100)
    private String storageTypeStr;

    /**
     * localURI.
     */
    @ApiModelProperty(	example="12345", allowEmptyValue = true, position =3, accessMode = ApiModelProperty.AccessMode.READ_ONLY, value = "Full URI Result", required = true)
    @Size(min = 1, max = ValidationConstants.MAX_LENGTH_DEFAULT)
    @Column(name = Columns.LOCAL_URI, unique = false, nullable = true,columnDefinition = "VARCHAR(400)",length = 400)
    private String localUri;


    public LocalURI() {
    }

    public LocalURI(String localUri, CanonicalURILanguage cul, StorageType st) {
        this.localUri = localUri;
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

    public void setLocalUri(String localUri) {
        this.localUri = localUri;
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
            if (Utils.isValidString(this.localUri)) {
                f.add(new SearchCriteria("localUri", this.localUri, SearchOperation.EQUAL));
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
            if (Utils.isValidString(this.localUri)) {
                f.add(new SearchCriteria("localUri", this.localUri, SearchOperation.EQUAL));
            }
            if (Utils.isValidString(this.localUri)) {
                f.add(new SearchCriteria("canonicalURILanguageStr", this.canonicalURILanguageStr, SearchOperation.EQUAL));
            }
            if (Utils.isValidString(this.localUri)) {
                f.add(new SearchCriteria("storageTypeStr", this.storageTypeStr, SearchOperation.EQUAL));
            }
        }

        return f;
    }

    public void merge(LocalURI other){
        this.localUri = other.localUri;
    }

}
