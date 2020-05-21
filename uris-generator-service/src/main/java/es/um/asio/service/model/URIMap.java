package es.um.asio.service.model;

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
import java.util.InvalidPropertiesFormatException;
import java.util.List;

@Entity
@Table(name=URIMap.TABLE, indexes = { @Index(columnList = URIMap.Columns.ENTITY), @Index(columnList = URIMap.Columns.PROPERTY), @Index(columnList = URIMap.Columns.ONTOLOGY_URI), @Index(columnList = URIMap.Columns.IDENTIFIER)})
@Getter
@Setter
@ToString(includeFieldNames = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class URIMap {

    /**
     * Version ID.
     */
    private static final long serialVersionUID = -8605786237765754616L;

    /**
     * The id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = URIMap.Columns.ID)
    @EqualsAndHashCode.Include
    @ApiModelProperty(hidden = true)
    private long id;

    /**
     * Base URL.
     */
    @ApiModelProperty(	example="/parent1/parent2/parent3", position =1, accessMode = ApiModelProperty.AccessMode.READ_WRITE, value = "Optional: Parent Base URL, if not present, path / will by inferred", required = false)
    @Size(min = 1, max = ValidationConstants.MAX_LENGTH_DEFAULT)
    @Column(name = URIMap.Columns.BASE_URI, nullable = true, length = ValidationConstants.MAX_LENGTH_DEFAULT)
    private String baseURI;

    /**
     * Qualified URL.
     */
    @ApiModelProperty(hidden = true, position =5, accessMode = ApiModelProperty.AccessMode.READ_ONLY, value = "Only Read: generated final URI", required = false)
    @Size(min = 1, max = ValidationConstants.MAX_LENGTH_DEFAULT)
    @Column(name = Columns.QUALIFIED_URI, nullable = true, length = ValidationConstants.MAX_LENGTH_DEFAULT)
    private String qualifiedURI;

    /**
     * ENTITY name.
     */
    @ApiModelProperty(	example="entitiyName", position =2, accessMode = ApiModelProperty.AccessMode.READ_WRITE, value = "Required: Name of Entity: the class name", required = true)
    @Size(min = 1, max = ValidationConstants.MAX_LENGTH_DEFAULT)
    @Column(name = Columns.ENTITY, nullable = false, length = ValidationConstants.MAX_LENGTH_DEFAULT)
    private String entity;

    /**
     * PROPERTY name.
     */
    @ApiModelProperty(	example="propertyName", position =3, readOnly=false, value = "Optional: Property name, only required for properties", required = true)
    @Size(min = 1, max = ValidationConstants.MAX_LENGTH_DEFAULT)
    @Column(name = Columns.PROPERTY, nullable = true, length = ValidationConstants.MAX_LENGTH_DEFAULT)
    private String property;

    /**
     * URI.
     */
    @Size(min = 1, max = ValidationConstants.MAX_LENGTH_DEFAULT)
    @Column(name = Columns.ONTOLOGY_URI, nullable = true, length = ValidationConstants.MAX_LENGTH_DEFAULT)
    private String ontologyURI;

    /**
     * Identifier.
     */
    @Size(min = 1, max = ValidationConstants.MAX_LENGTH_DEFAULT)
    @Column(name = Columns.IDENTIFIER, nullable = true, length = ValidationConstants.MAX_LENGTH_DEFAULT)
    private String identifier;

    /**
     * Is Opaque.
     */
    @Column(name = Columns.IS_OPAQUE)
    private Boolean isOpaque = false;

    /**
     * Is Entity.
     */
    @Column(name = Columns.IS_ENTITY)
    private Boolean isEntity = false;

    /**
     * Is Property.
     */
    @Column(name = Columns.IS_PROPERTY)
    private Boolean isProperty = false;

    /**
     * Is Instance.
     */
    @Column(name = Columns.IS_INSTANCE)
    private Boolean isInstance = false;

    /**
     * Table name.
     */
    public static final String TABLE = "URI_MAP";


    /**
     * Version
     */
    @Version
    private Integer version;

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
        protected static final String BASE_URI = "BASE_URI";

        /**
         * BASE URL column.
         */
        protected static final String QUALIFIED_URI = "QUALIFIED_URI";

        /**
         * Entity column.
         */
        protected static final String ENTITY = "ENTITY";

        /**
         * Property column.
         */
        protected static final String PROPERTY = "PROPERTY";

        /**
         * URI column.
         */
        protected static final String ONTOLOGY_URI = "ONTOLOGY_URI";

        /**
         * IDENTIFIER column.
         */
        protected static final String IDENTIFIER = "IDENTIFIER";

        /**
         * IS_Instance column.
         */
        protected static final String IS_INSTANCE = "IS_INSTANCE";

        /**
         * IS_OPAQUE column.
         */
        protected static final String IS_OPAQUE = "IS_OPAQUE";

        /**
         * IS_ENTITY column.
         */
        protected static final String IS_ENTITY = "IS_ENTITY";

        /**
         * IS_PROPERTY column.
         */
        protected static final String IS_PROPERTY = "IS_PROPERTY";

    }

    public URIMapFilter buildFilterByEntity() {
        URIMapFilter f = new URIMapFilter();
        if (this.id != 0) {
            f.add(new SearchCriteria(Columns.ID, this.id, SearchOperation.EQUAL));
        }
        if (this.baseURI != null) {
            f.add(new SearchCriteria(Columns.BASE_URI, this.baseURI, SearchOperation.EQUAL));
        }
        if (this.qualifiedURI != null) {
            f.add(new SearchCriteria(Columns.QUALIFIED_URI, this.qualifiedURI, SearchOperation.EQUAL));
        }
        if (this.entity != null) {
            f.add(new SearchCriteria(Columns.ENTITY, this.entity, SearchOperation.EQUAL));
        }
        if (this.property != null) {
            f.add(new SearchCriteria(Columns.PROPERTY, this.property, SearchOperation.EQUAL));
        }
        if (this.ontologyURI != null) {
            f.add(new SearchCriteria(Columns.ONTOLOGY_URI, this.ontologyURI, SearchOperation.EQUAL));
        }
        if (this.identifier != null) {
            f.add(new SearchCriteria(Columns.IDENTIFIER, this.identifier, SearchOperation.EQUAL));
        }
        if (this.isInstance != null) {
            f.add(new SearchCriteria(Columns.IS_INSTANCE, this.isInstance, SearchOperation.EQUAL));
        }
        if (this.isOpaque != null) {
            f.add(new SearchCriteria(Columns.IS_OPAQUE, this.isOpaque, SearchOperation.EQUAL));
        }
        if (this.isEntity != null) {
            f.add(new SearchCriteria(Columns.IS_ENTITY, this.isEntity, SearchOperation.EQUAL));
        }
        if (this.isProperty != null) {
            f.add(new SearchCriteria(Columns.IS_PROPERTY, this.isProperty, SearchOperation.EQUAL));
        }
        return f;
    }

    public URIMapFilter buildFilterByEntityUniqueProperties() {
        URIMapFilter f = new URIMapFilter();
        if (this.id != 0) {
            f.add(new SearchCriteria("id", this.id, SearchOperation.EQUAL));
        }
        if (this.baseURI != null) {
            f.add(new SearchCriteria("baseURI", this.baseURI, SearchOperation.EQUAL));
        }
        if (this.identifier != null && !this.isInstance) {
            f.add(new SearchCriteria("identifier", this.identifier, SearchOperation.EQUAL));
        }
        if (this.isInstance != null) {
            f.add(new SearchCriteria("isInstance", this.isInstance, SearchOperation.EQUAL));
        }
        if (this.entity != null) {
            f.add(new SearchCriteria("entity", this.entity, SearchOperation.EQUAL));
        }
        if (this.property != null) {
            f.add(new SearchCriteria("property", this.property, SearchOperation.EQUAL));
        }
        return f;
    }

    public void completeEntity(List<URIMap> urisMap) throws InvalidPropertiesFormatException {
        if (entity !=null && property != null) {
            isProperty = true;
        } else if (entity!=null) {
            isEntity = true;
        }
        if (identifier == null) {
            identifier = calculateIdentifier(urisMap,isOpaque);
        } else {
            if (isOpaque && !Utils.isValidUUID(identifier)) {
                identifier = Utils.getUUIDFromString(identifier);
            }
        }
        if  (baseURI == null || baseURI == "") {
            baseURI = "/";
        } else if (!Utils.isValidURL(baseURI)){
            throw new InvalidPropertiesFormatException("Invalid baseURI: " +baseURI);
        }
        qualifiedURI = (baseURI.substring(baseURI.length() - 1) == "/") ? baseURI : ((baseURI + "/") +
                entity + "/" +
                (((property != null) && (property == "")) ? property + "/" : "") +
                ((!isInstance) ? identifier : ""));
    }

    public String calculateIdentifier(List<URIMap> urisMap, boolean isOpaque) {

        if (urisMap.size() == 0) {
            if (isOpaque) {
                return Utils.getUUIDFromString(String.valueOf(1));
            } else
                return "1";
        } else {
            URIMap first = urisMap.get(0);
            if (isOpaque) {
                if (Utils.isValidUUID(first.getIdentifier())) {
                    return (first.getIdentifier());
                } else {
                    return Utils.getUUIDFromString(first.getIdentifier());
                }
            } else {
                return (first.getIdentifier()!=null)?first.getIdentifier():"1";
            }
        }

    }

    public void merge(URIMap o) {
        this.setBaseURI(o.getBaseURI());
        this.setQualifiedURI(o.getQualifiedURI());
        this.setEntity(o.getEntity());
        this.setProperty(o.getProperty());
        this.setOntologyURI(o.getOntologyURI());
        this.setIdentifier(o.getIdentifier());
        this.setIsOpaque(o.getIsOpaque());
        this.setIsEntity(o.getIsEntity());
        this.setIsProperty(o.getIsProperty());
    }



}
