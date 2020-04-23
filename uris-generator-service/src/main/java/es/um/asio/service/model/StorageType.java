package es.um.asio.service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import es.um.asio.service.filter.SearchCriteria;
import es.um.asio.service.filter.SearchOperation;
import es.um.asio.service.filter.StorageTypeFilter;
import es.um.asio.service.filter.TypeFilter;
import es.um.asio.service.util.Utils;
import es.um.asio.service.util.ValidationConstants;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.SafeHtml;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity
@Table(name = StorageType.TABLE)
@Getter
@ToString(includeFieldNames = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class StorageType {
    /**
     * Version ID.
     */
    private static final long serialVersionUID = -8605786237765754677L;

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
     * NAME.
     */
    @ApiModelProperty(	example="WIKIBASE",allowEmptyValue = true, position =2, readOnly=false, value = "Required: Name of Storage.", required = true)
    @Size(min = 1, max = ValidationConstants.MAX_LENGTH_DEFAULT)
    @SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
    @Column(name = Columns.NAME, nullable = false,columnDefinition = "VARCHAR(100)",length = 100)
    private String name;

    /**
     * API_URL.
     */
    @ApiModelProperty(	example="",allowEmptyValue = true, position =3, readOnly=false, value = "Optional: API URL.", required = false)
    @Size(min = 1, max = ValidationConstants.MAX_LENGTH_DEFAULT)
    @SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
    @Column(name = Columns.API_URL, unique = true, nullable = true,columnDefinition = "VARCHAR(400)",length = 400)
    private String apiURL;

    /**
     * ENDPOINT_URL.
     */
    @ApiModelProperty(	example="",allowEmptyValue = true, position =4, readOnly=false, value = "Optional: SPARQL ENDPOINT URL.", required = false)
    @Size(min = 1, max = ValidationConstants.MAX_LENGTH_DEFAULT)
    @SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
    @Column(name = Columns.ENDPOINT_URL, nullable = true,columnDefinition = "VARCHAR(400)",length = 400)
    private String endPointURL;

    /**
     * URL SCHEMA.
     */
    @ApiModelProperty(	example="",allowEmptyValue = true, position =5, readOnly=false, value = "Optional: URI Schema in local storage.", required = false)
    @Size(min = 1, max = ValidationConstants.MAX_LENGTH_DEFAULT)
    @SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
    @Column(name = Columns.URI_SCHEMA, nullable = true,columnDefinition = "VARCHAR(400)",length = 400)
    private String schemaURI;

    /**
     * Relation Bidirectional LocalURI OneToMany
     */
    @JsonIgnore
    @OneToMany(mappedBy = "storageType", cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private Set<LocalURI> localURIs;

    public StorageType() {
    }

    public StorageType(@Size(min = 1, max = ValidationConstants.MAX_LENGTH_DEFAULT) @SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE) String name) {
        this.name = name;
    }

    public StorageType(@Size(min = 1, max = ValidationConstants.MAX_LENGTH_DEFAULT) @SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE) String name, @Size(min = 1, max = ValidationConstants.MAX_LENGTH_DEFAULT) @SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE) String apiURL, @Size(min = 1, max = ValidationConstants.MAX_LENGTH_DEFAULT) @SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE) String endPointURL, @Size(min = 1, max = ValidationConstants.MAX_LENGTH_DEFAULT) @SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE) String schemaURI) {
        this.name = (name!=null)?name.toLowerCase().trim():null;
        this.apiURL = apiURL;
        this.endPointURL = endPointURL;
        this.schemaURI = schemaURI;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getApiURL() {
        return apiURL;
    }

    public void setApiURL(String apiURL) {
        this.apiURL = apiURL;
    }

    public String getEndPointURL() {
        return endPointURL;
    }

    public void setEndPointURL(String endPointURL) {
        this.endPointURL = endPointURL;
    }

    public String getSchemaURI() {
        return schemaURI;
    }

    public void setSchemaURI(String schemaURI) {
        this.schemaURI = schemaURI;
    }

    /**
     * Table name.
     */
    public static final String TABLE = "STORAGE_TYPE";

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
         * NAME TYPE COLUMN.
         */
        protected static final String NAME = "NAME";

        /**
         * NAPI_URL COLUMN.
         */
        protected static final String API_URL = "API_URL";

        /**
         * ENDPOINT_URL COLUMN.
         */
        protected static final String ENDPOINT_URL = "ENDPOINT_URL";

        /**
         * ENDPOINT_URL COLUMN.
         */
        protected static final String URI_SCHEMA = "URI_SCHEMA";
    }

    public StorageTypeFilter buildFilterByEntity() {
        StorageTypeFilter f = new StorageTypeFilter();
        if (this.id > 0) {
            f.add(new SearchCriteria("id", this.id, SearchOperation.EQUAL));
        }
        if (this.name != null && !this.name.equals("")) {
            f.add(new SearchCriteria("name", this.name, SearchOperation.EQUAL));
        }
        if (this.apiURL != null && !this.apiURL.equals("")) {
            f.add(new SearchCriteria("apiURL", this.apiURL, SearchOperation.EQUAL));
        }
        if (this.endPointURL != null && !this.endPointURL.equals("")) {
            f.add(new SearchCriteria("endPointURL", this.endPointURL, SearchOperation.EQUAL));
        }
        if (this.schemaURI != null && !this.schemaURI.equals("")) {
            f.add(new SearchCriteria("schemaURI", this.schemaURI, SearchOperation.EQUAL));
        }
        return f;
    }

    public StorageTypeFilter buildFilterByEntityUniqueProperties() {
        StorageTypeFilter f = new StorageTypeFilter();
        if (this.id > 0) {
            f.add(new SearchCriteria("id", this.id, SearchOperation.EQUAL));
        } else {
            if (Utils.isValidString(this.name)) {
                f.add(new SearchCriteria("name", this.name, SearchOperation.EQUAL));

            }
        }
        return f;
    }

    public void merge(StorageType other){
        this.apiURL = other.apiURL;
        this.endPointURL = other.endPointURL;
        this.schemaURI = other.schemaURI;
    }

    @Override
    public String toString() {
        return "StorageType{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", apiURL='" + apiURL + '\'' +
                ", endPointURL='" + endPointURL + '\'' +
                ", schemaURI='" + schemaURI + '\'' +
                '}';
    }
}
