package es.um.asio.service.model;

import es.um.asio.service.filter.SearchCriteria;
import es.um.asio.service.filter.SearchOperation;
import es.um.asio.service.filter.StorageTypeFilter;
import es.um.asio.service.filter.TypeFilter;
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
    @Column(name = Columns.API_URL, nullable = false,columnDefinition = "VARCHAR(400)",length = 400)
    private String apiURL;

    /**
     * ENDPOINT_URL.
     */
    @ApiModelProperty(	example="",allowEmptyValue = true, position =4, readOnly=false, value = "Optional: SPARQL ENDPOINT URL.", required = false)
    @Size(min = 1, max = ValidationConstants.MAX_LENGTH_DEFAULT)
    @SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
    @Column(name = Columns.ENDPOINT_URL, nullable = false,columnDefinition = "VARCHAR(400)",length = 400)
    private String endPointURL;

    /**
     * URL SCHEMA.
     */
    @ApiModelProperty(	example="",allowEmptyValue = true, position =5, readOnly=false, value = "Optional: URI Schema in local storage.", required = false)
    @Size(min = 1, max = ValidationConstants.MAX_LENGTH_DEFAULT)
    @SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
    @Column(name = Columns.URI_SCHEMA, nullable = false,columnDefinition = "VARCHAR(400)",length = 400)
    private String schemaURI;

    /**
     * Relation Bidirectional LocalURI OneToMany
     */
    @OneToMany(mappedBy = "storageType", cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private Set<LocalURI> localURIs;

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
            f.add(new SearchCriteria(Columns.ID, this.id, SearchOperation.EQUAL));
        }
        if (this.name != null && !this.name.equals("")) {
            f.add(new SearchCriteria(Columns.NAME, this.name, SearchOperation.EQUAL));
        }
        if (this.apiURL != null && !this.apiURL.equals("")) {
            f.add(new SearchCriteria(Columns.API_URL, this.apiURL, SearchOperation.EQUAL));
        }
        if (this.endPointURL != null && !this.endPointURL.equals("")) {
            f.add(new SearchCriteria(Columns.ENDPOINT_URL, this.endPointURL, SearchOperation.EQUAL));
        }
        if (this.schemaURI != null && !this.schemaURI.equals("")) {
            f.add(new SearchCriteria(Columns.URI_SCHEMA, this.schemaURI, SearchOperation.EQUAL));
        }
        return f;
    }

    public StorageTypeFilter buildFilterByEntityUniqueProperties() {
        StorageTypeFilter f = new StorageTypeFilter();
        if (this.id > 0) {
            f.add(new SearchCriteria(Columns.ID, this.id, SearchOperation.EQUAL));
        }
        return f;
    }
}
