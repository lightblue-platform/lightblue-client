package com.redhat.lightblue.client.response;

import java.util.HashSet;
import java.util.Set;

import com.redhat.lightblue.client.model.DataError;
import com.redhat.lightblue.client.model.Error;

/**
 * Exception thrown when a response from lightblue has errors or data errors.
 * The client can call e.exists(ERR_REST_CRUD_REST_SAVE)
 *
 * @author ykoer
 */
public class LightblueException extends Exception {

    private static final long serialVersionUID = 1L;

    public static final String ERR_NO_OBJECT_TYPE = "NO_OBJECT_TYPE";
    public static final String ERR_INVALID_OBJECTTYPE = "INVALID_OBJECTTYPE";
    public static final String ERR_INVALID_FIELD = "INVALID_FIELD";
    public static final String ERR_INVALID_COMPARISON = "INVALID_COMPARISON";
    public static final String ERR_MONGO_CRUD_INVALID_OBJECT = "mongo-crud:InvalidObject";
    public static final String ERR_MONGO_CRUD_DUPLICATE = "mongo-crud:Duplicate";
    public static final String ERR_MONGO_CRUD_INSERTION_ERROR = "mongo-crud:InsertionError";
    public static final String ERR_MONGO_CRUD_SAVE_ERROR = "mongo-crud:SaveError";
    public static final String ERR_MONGO_CRUD_UPDATE_ERROR = "mongo-crud:UpdateError";
    public static final String ERR_MONGO_CRUD_NO_ACCESS = "mongo-crud:NoAccess";
    public static final String ERR_MONGO_CRUD_CONNECTION_ERROR = "mongo-crud:ConnectionError";
    public static final String ERR_MONGO_CRUD_EMPTY_DOCUMENTS = "mongo-crud:EmptyDocuments";
    public static final String ERR_MONGO_CRUD_EMPTY_VALUE_LIST = "mongo-crud:EmptyValueList";
    public static final String ERR_MONGO_CRUD_NULL_QUERY = "mongo-crud:NullQuery";
    public static final String ERR_MONGO_CRUD_NULL_PROJECTION = "mongo-crud:NullProjection";
    public static final String ERR_MONGO_CRUD_SAVE_CLOBBERS_HIDDEN_FIELDS = "mongo-crud:SaveClobblersHiddenFields";
    public static final String ERR_MONGO_CRUD_TRANSLATION_ERROR = "mongo-crud:TranslationError";
    public static final String ERR_MONGO_CRUD_ENTITY_INDEX_NOT_CREATED = "mongo-crud:EntityIndexNotCreated";
    public static final String ERR_MONGO_CRUD_INVALID_INDEX_FIELD = "mongo-crud:InvalidIndexField";
    public static final String ERR_MONGO_METADATA_DUPLICATE_METADATA = "mongo-metadata:DuplicateMetadata";
    public static final String ERR_MONGO_METADATA_UNKNOWN_VERSION = "mongo-metadata:UnknownVersion";
    public static final String ERR_MONGO_METADATA_DB_ERROR = "mongo-metadata:DatabaseError";
    public static final String ERR_MONGO_METADATA_MISSING_ENTITY_INFO = "mongo-metadata:MissingEntityInfo";
    public static final String ERR_MONGO_METADATA_DISABLED_DEFAULT_VERSION = "mongo-metadata:DisabledDefaultVersion";
    public static final String ERR_MONGO_METADATA_INVALID_DATASTORE = "mongo-metadata:InvalidDatastore";
    public static final String ERR_MONGO_METADATA_NEW_STATUS_IS_NULL = "mongo-metadata:NewStatusIsNull";
    public static final String ERR_MONGO_METADATA_CANNOT_DELETE = "mongo-metadata:CannotDeleteEntity";
    public static final String ERR_MONGO_METADATA_INVALID_ID = "mongo-metadata:Invalid_id_defn";
    public static final String ERR_MONGO_METADATA_UPDATE_INVALIDATES_METADATA = "mongo-metadata:UpdateInvalidatesMetadata";
    public static final String ERR_CRUD_ARRAY_TOO_SMALL = "crud:ArrayTooSmall";
    public static final String ERR_CRUD_ARRAY_TOO_LARGE = "crud:ArrayTooLarge";
    public static final String ERR_CRUD_INVALID_ENUM = "crud:InvalidEnum";
    public static final String ERR_CRUD_VALUE_TOO_SMALL = "crud:ValueTooSmall";
    public static final String ERR_CRUD_VALUE_TOO_LARGE = "crud:ValueTooLarge";
    public static final String ERR_CRUD_REQUIRED = "crud:Required";
    public static final String ERR_CRUD_TOO_SHORT = "crud:TooShort";
    public static final String ERR_CRUD_TOO_LONG = "crud:TooLong";
    public static final String ERR_CRUD = "crud";
    public static final String ERR_CRUD_NO_ACCESS = "crud:NoAccess";
    public static final String ERR_CRUD_NO_FIELD_INSERT_ACCESS = "crud:insert:NoFieldAccess";
    public static final String ERR_CRUD_NO_FIELD_UPDATE_ACCESS = "crud:update:NoFieldAccess";
    public static final String ERR_CRUD_INVALID_ENTITY = "crud:InvalidEntity";
    public static final String ERR_CRUD_INVALID_DEREFERENCE = "crud:InvalidDeference";
    public static final String ERR_CRUD_INVALID_ASSIGNMENT = "crud:InvalidAssignment";
    public static final String ERR_CRUD_INVALID_HOOK = "crud:InvalidHook";
    public static final String ERR_CRUD_INCOMPATIBLE_DEREFERENCE = "crud:IncompatibleDereference";
    public static final String ERR_CRUD_INCOMPATIBLE_ASSIGNMENT = "crud:IncompatibleAssignment";
    public static final String ERR_CRUD_PATTERN_NOT_EXPECTED = "crud:PatternNotExpected";
    public static final String ERR_CRUD_REQUIRED_INSERTION_INDEX = "crud:InsertionRequiresIndex";
    public static final String ERR_CRUD_REQUIRED_ARRAY = "crud:ArrayRequired";
    public static final String ERR_CRUD_EXPECTED_OBJECT_VALUE = "crud:ObjectValueExpected";
    public static final String ERR_CRUD_EXPECTED_VALUE = "crud:ValueExpected";
    public static final String ERR_CRUD_EXPECTED_SIMPLE_ARRAY = "crud:SimpleArrayExpected";
    public static final String ERR_CRUD_EXPECTED_ARRAY_FIELD = "crud:ArrayFieldExpected";
    public static final String ERR_CRUD_EXPECTED_SIMPLE_FIELD_OR_SIMPLE_ARRAY = "crud:SimpleFieldOrSimpleArrayExpected";
    public static final String ERR_CRUD_EXPECTED_OBJECT_ARRAY = "crud:ExpectedObjectArray";
    public static final String ERR_CRUD_EXPECTED_ARRAY = "crud:ExpectedArray";
    public static final String ERR_CRUD_EXPECTED_ARRAY_ELEMENT = "crud:ExpectedArrayElement";
    public static final String ERR_CRUD_FIELD_NOT_ARRAY = "crud:FieldNotArray";
    public static final String ERR_CRUD_FIELD_NOT_THERE = "crud:FieldNotThere";
    public static final String ERR_CRUD_CANT_ACCESS = "crud:CannotAccess";
    public static final String ERR_CRUD_ASSIGNMENT = "crud:AssignmentError";
    public static final String ERR_CRUD_NO_CONSTRAINT = "crud:NoConstraint";
    public static final String ERR_CRUD_CONFIG_NOT_VALID = "crud:ConfigurationNotValid";
    public static final String ERR_CRUD_CANNOT_LOAD_METADATA = "crud:CannotLoadMetadata";
    public static final String ERR_CRUD_DISABLED_METADATA = "crud:DisabledMetadataVersion";
    public static final String ERR_CRUD_METADATA_APPEARS_TWICE = "crud:MetadataAppearsTwice";
    public static final String ERR_CRUD_UNKNOWN_ENTITY = "crud:UnknownEntity";
    public static final String ERR_CRUD_AUTH_FAILED = "crud:AuthFailed";
    public static final String ERR_CRUD_DATASOURCE_TIMEOUT = "crud:DataSourceTimeout";
    public static final String ERR_CRUD_DATASOURCE_UNKNOWN = "crud:DataSourceUnknown";
    public static final String ERR_ASSOC_CANNOT_CREATE_CHOOSER = "assoc:CannotCreateQueryPlanChooser";
    public static final String ERR_ASSOC_UNRELATED_ENTITY_Q = "assoc:unsupported:QueryForUnrelatedEntities";
    public static final String ERR_ASSOC_MORE_THAN_TWO_Q = "assoc:unsupported:QueryForMoreThanTwoEntities";
    public static final String ERR_ASSOC_REWRITE = "assoc:QueryRewriteError";
    public static final String ERRASSOC__ARRAY_EXPECTED = "assoc:ArrayFieldExpected";
    public static final String ERR_ASSOC_CANNOT_FIND_FIELD = "assoc:NoField";
    public static final String ERR_METADATA_DUPLICATE_FIELD = "metadata:DuplicateField";
    public static final String ERR_METADATA_DUPLICATE_ENUM = "metadata:DuplicateEnum";
    public static final String ERR_METADATA_INVALID_ARRAY_REFERENCE = "metadata:InvalidArrayReference";
    public static final String ERR_METADATA_INVALID_FIELD_REFERENCE = "metadata:InvalidFieldReference";
    public static final String ERR_METADATA_INVALID_REDIRECTION = "metadata:InvalidRedirection";
    public static final String ERR_METADATA_INVALID_THIS = "metadata:Invalid$This";
    public static final String ERR_METADATA_INVALID_PARENT = "metadata:Invalid$Parent";
    public static final String ERR_METADATA_FIELD_WRONG_TYPE = "metadata:FieldWrongType";
    public static final String ERR_METADATA_PARSE_MISSING_ELEMENT = "metadata:ParseMissingElement";
    public static final String ERR_METADATA_PARSE_INVALID_STATUS = "metadata:ParseInvalidStatus";
    public static final String ERR_METADATA_INVALID_ARRAY_ELEMENT_TYPE = "metadata:InvalidArrayElementType";
    public static final String ERR_METADATA_ILL_FORMED_METADATA = "metadata:IllFormedMetadata";
    public static final String ERR_METADATA_INVALID_BACKEND = "metadata:InvalidBackend";
    public static final String ERR_METADATA_INVALID_INDEX = "metadata:InvalidIndex";
    public static final String ERR_METADATA_INVALID_ENUM = "metadata:InvalidEnum";
    public static final String ERR_METADATA_INVALID_HOOK = "metadata:InvalidHook";
    public static final String ERR_METADATA_UNKNOWN_BACKEND = "metadata:UnknownBackend";
    public static final String ERR_METADATA_INVALID_CONSTRAINT = "metadata:InvalidConstraint";
    public static final String ERR_METADATA_INVALID_TYPE = "metadata:InvalidType";
    public static final String ERR_METADATA_INCOMPATIBLE_VALUE = "metadata:IncompatibleValue";
    public static final String ERR_METADATA_INCOMPATIBLE_FIELDS = "metadata:IncompatibleFields";
    public static final String ERR_METADATA_CONFIG_NOT_VALID = "metadata:ConfigurationNotValid";
    public static final String ERR_METADATA_CONFIG_NOT_FOUND = "metadata:ConfigurationNotFound";
    public static final String ERR_METADATA_NOT_A_NUMBER_TYPE = "metadata:NotANumberType";
    public static final String ERR_METADATA_COMPARE_NOT_SUPPORTED = "metadata:CompareNotSupported";
    public static final String ERR_METADATA_CAST_NOT_SUPPORTED = "metadata:CastNotSupported";
    public static final String ERR_METADATA_TO_JSON_NOT_SUPPORTED = "metadata:ToJsonNotSupported";
    public static final String ERR_METADATA_FROM_JSON_NOT_SUPPORTED = "metadata:FromJsonNotSupported";
    public static final String METADATA_ERR_INVALID_VERSION = "metadata:InvalidVersion";
    public static final String METADATA_ERR_INVALID_VERSION_NUMBER = "metadata:InvalidVersionNumber";
    public static final String METADATA_ERR_EMPTY_METADATA_NAME = "metadata:EmptyMetadataName";
    public static final String METADATA_ERR_METADATA_WITH_NO_FIELDS = "metadata:MetadataWithNoFields";
    public static final String METADATA_ERR_INVALID_DEFAULT_VERSION = "metadata:InvalidDefaultVersion";
    public static final String METADATA_ERR_INVALID_CONTEXT = "metadata:InvalidContext";
    public static final String METADATA_ERR_AUTH_FAILED = "metadata:AuthFailed";
    public static final String METADATA_ERR_DATASOURCE_TIMEOUT = "metadata:DataSourceTimeout";
    public static final String METADATA_ERR_DATASOURCE_UNKNOWN = "metadata:DataSourceUnknown";
    public static final String ERR_VALIDATION_FAILED = "validation:fail";
    public static final String ERR_QUERY_API_INVALID_ARRAY_UPDATE_EXPRESSION = "query-api:InvalidArrayUpdateExpression";
    public static final String ERR_QUERY_API_INVALID_ARRAY_COMPARISON_EXPRESSION = "query-api:InvalidArrayComparisonExpression";
    public static final String ERR_QUERY_API_INVALID_PROJECTION = "query-api:InvalidProjection";
    public static final String ERR_QUERY_API_INVALID_ARRAY_RANGE_PROJECTION = "query-api:InvalidArrayRangeProjection";
    public static final String ERR_QUERY_API_INVALID_COMPARISON_EXPRESSION = "query-api:InvalidComparisonExpression";
    public static final String ERR_QUERY_API_INVALID_LOGICAL_EXPRESSION = "query-api:InvalidLogicalExpression";
    public static final String ERR_QUERY_API_INVALID_UPDATE_EXPRESSION = "query-api:InvalidUpdateExpression";
    public static final String ERR_QUERY_API_INVALID_QUERY = "query-api:InvalidQuery";
    public static final String ERR_QUERY_API_INVALID_REGEX_EXPRESSION = "query-api:InvalidRegexExpression";
    public static final String ERR_QUERY_API_INVALID_RVALUE_EXPRESSION = "query-api:InvalidRValueExpression";
    public static final String ERR_QUERY_API_INVALID_SET_EXPRESSION = "query-api:InvalidSetExpression";
    public static final String ERR_QUERY_API_INVALID_SORT = "query-api:InvalidSort";
    public static final String ERR_QUERY_API_INVALID_UNSET_EXPRESSION = "query-api:InvalidUnsetExpression";
    public static final String ERR_QUERY_API_INVALID_VALUE = "query-api:InvalidValue";
    public static final String ERR_QUERY_API_OPERATOR_LIST_NULL_OR_EMPTY = "query-api:OperatorListNullOfEmpty";
    public static final String ERR_QUERY_API_UNSUPPORTED_OPERATOR = "query-api:UnsupportedOperator";
    public static final String ERR_QUERY_API_INVALID_VALUE_BINDING = "query-api:InvalidValueBinding";
    public static final String ERR_UTIL_NEXT_ON_EMPTY_ITR = "util:NextOnEmptyIterator";
    public static final String ERR_UTIL_REMOVE_ON_EMPTY_ITR = "util:RemoveOnEmptyIterator";
    public static final String ERR_UTIL_CANT_SET_EMPTY_PATH_VALUE = "util:CantSetEmptyPathValue";
    public static final String ERR_UTIL_IS_NOT_A_CONTAINER = "util:IsNotAContainer";
    public static final String ERR_UTIL_PARENT_DOESNT_EXIST = "util:ParentDoesNotExist";
    public static final String ERR_UTIL_INVALID_INDEXED_ACCESS = "util:InvalidIndexedAccess";
    public static final String ERR_UTIL_EXPECTED_ARRAY_INDEX = "util:ExpectedArrayIndex";
    public static final String ERR_UTIL_NULL_VALUE_PASSED_TO_PUSH = "util:NullValuePassedToPush";
    public static final String ERR_UTIL_CANT_POP_EMPTY_PATH = "util:CantPopEmptyPath";
    public static final String ERR_UTIL_CANT_SET_LAST_SEGMENT_ON_EMPTY_PATH = "util:CantSetLastSegmentOnEmptyPath";
    public static final String ERR_UTIL_UNEXPECTED_DOT = "util:UnexpectedDot";
    public static final String ERR_UTIL_UNEXPECTED_WHITESPACE = "util:UnexpectedWhitespace";
    public static final String ERR_UTIL_UNEXPECTED_CHARACTER = "util:UnexpectedCharacter";
    public static final String ERR_UTIL_JSON_SCHEMA_INVALID = "util:JsonSchemaInvalid";
    public static final String ERR_LDAP_UNSUPPORTED_FEATURE = "ldap:UnsupportedFeature:";
    public static final String ERR_UNSUPPORTED_FEATURE_OBJECT_ARRAY = ERR_LDAP_UNSUPPORTED_FEATURE + "ObjectArray";
    public static final String ERR_LDAP_UNDEFINED_UNIQUE_ATTRIBUTE = "ldap:UndefinedUniqueAttribute";
    public static final String ERR_RDBMS_DATASOURCE_NOT_FOUND = "rdbms:DatasourceNotFound";
    public static final String ERR_RDBMS_GET_CONNECTION_FAILED = "rdbms-util:GetConnectionFailed";
    public static final String ERR_RDBMS_GET_STATEMENT_FAILED = "rdbms:GetStatementFailed";
    public static final String ERR_RDBMS_EXECUTE_QUERY_FAILED = "rdbms:ExecuteQueryFailed";
    public static final String ERR_RDBMS_EXECUTE_UPDATE_FAILED = "rdbms:ExecuteUpdateFailed";
    public static final String ERR_RDBMS_BUILD_RESULT_FAILED = "rdbms:BuildResultFailed";
    public static final String ERR_RDBMS_ILL_FORMED_METADATA = "rdbms:IllFormedMetadata";
    public static final String ERR_RDBMS_NO_ACCESS = "rdbms:NoAccess";
    public static final String ERR_RDBMS_NO_OPERATOR = "rdbms:NoSupportedOperator";
    public static final String ERR_RDBMS_NO_PROJECTION = "rdbms:NoProjection";
    public static final String ERR_RDBMS_NO_GROUPBY = "rdbms:GroupByNotSupported";
    public static final String ERR_RDBMS_SUP_OPERATOR = "rdbms:NoSupportedProjection";
    public static final String ERR_RDBMS_SUP_QUERY = "rdbms:NoSupportedQuery";
    public static final String ERR_RDBMS_FIELD_REQUIRED = "rdbms-metadata:FieldRequired";
    public static final String ERR_RDBMS_WRONG_ROOT_NODE_NAME = "rdbms-metadata:WrongRootNodeName";
    public static final String ERR_RDBMS_WRONG_FIELD = "rdbms-metadata:WrongField";
    public static final String ERR_AUDIT_HOOK_MISSING_ID = "audit-hook:MissingID";
    public static final String ERR_REST_CRUD_REST_ERROR = "rest-crud:RestError";
    public static final String ERR_REST_CRUD_REST_FIND = "rest-crud:RestFindError";
    public static final String ERR_REST_CRUD_REST_INSERT = "rest-crud:RestInsertError";
    public static final String ERR_REST_CRUD_REST_UPDATE = "rest-crud:RestUpdateError";
    public static final String ERR_REST_CRUD_REST_SAVE = "rest-crud:RestSaveError";
    public static final String ERR_REST_CRUD_REST_DELETE = "rest-crud:RestDeleteError";
    public static final String ERR_REST_CRUD_NO_ENTITY_MATCH = "rest-crud:NoEntityMatch";
    public static final String ERR_REST_CRUD_NO_VERSION_MATCH = "rest-crud:NoVersionMatch";
    public static final String ERR_REST_CRUD_CANT_GET_MEDIATOR = "rest-crud:CantGetMediator";
    public static final String ERR_REST_CRUD_CANT_GET_TRANSLATOR = "rest-crud:CantGetTranslator";
    public static final String ERR_REST_METADATA_REST_ERROR = "rest-metadata:RestError";
    public static final String ERR_REST_METADATA_NO_ENTITY_NAME = "rest-metadata:NoEntityName";
    public static final String ERR_REST_METADATA_NO_ENTITY_VERSION = "rest-metadata:NoEntityVersion";
    public static final String ERR_REST_METADATA_NO_ENTITY_STATUS = "rest-metadata:NoEntityStatus";
    public static final String ERR_REST_METADATA_NO_NAME_MATCH = "rest-metadata:NoNameMatch";
    public static final String ERR_REST_METADATA_NO_VERSION_MATCH = "rest-metadata:NoVersionMatch";
    public static final String ERR_REST_METADATA_CANT_GET_METADATA = "rest-metadata:CantGetMetadata";
    public static final String ERR_REST_METADATA_CANT_GET_PARSER = "rest-metadata:CantGetParser";
    public static final String ERR_REST_METADATA_CANT_GET_TRANSLATOR = "rest-metadata:CantGetJsonTranslator";

    private DefaultLightblueResponse lightblueResponse;
    private Set<String> errorCodes;

    public LightblueException() {
        super();
    }

    public LightblueException(String message, DefaultLightblueResponse lightblueResponse) {
        super(message);
        this.lightblueResponse = lightblueResponse;
    }

    public LightblueException(String message, DefaultLightblueResponse lightblueResponse, Throwable cause) {
        super(message, cause);
        this.lightblueResponse = lightblueResponse;
    }

    public DefaultLightblueResponse getLightblueResponse() {
        return lightblueResponse;
    }

    public void setLightblueResponse(DefaultLightblueResponse lightblueResponse) {
        this.lightblueResponse = lightblueResponse;
    }

    public boolean exists(String errorCode) {
        return getErrorCodes().contains(errorCode);
    }

    public Set<String> getErrorCodes() {
        if (errorCodes == null || lightblueResponse == null) {
            errorCodes = new HashSet<>();
        } else {
            return errorCodes;
        }

        if (lightblueResponse.getErrors() != null) {
            for (Error e : lightblueResponse.getErrors()) {
                errorCodes.add(e.getErrorCode());
            }
        }
        if (lightblueResponse.getDataErrors() != null) {
            for (DataError de : lightblueResponse.getDataErrors()) {
                if (de.getErrors() != null) {
                    for (Error e : de.getErrors()) {
                        errorCodes.add(e.getErrorCode());
                    }
                }
            }
        }
        return errorCodes;
    }

    @Override
    public String getMessage() {
        if (lightblueResponse != null)
            return super.getMessage() + lightblueResponse.getText();
        else
            return super.getMessage();
    }
}
