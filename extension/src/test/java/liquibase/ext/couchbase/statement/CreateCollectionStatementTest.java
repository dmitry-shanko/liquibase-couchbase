package liquibase.ext.couchbase.statement;

import com.couchbase.client.core.error.CollectionExistsException;
import com.wdt.couchbase.Keyspace;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import liquibase.ext.couchbase.operator.BucketOperator;
import liquibase.ext.couchbase.operator.ClusterOperator;
import static com.wdt.couchbase.Keyspace.keyspace;
import static common.constants.TestConstants.TEST_BUCKET;
import static common.constants.TestConstants.TEST_COLLECTION;
import static common.constants.TestConstants.TEST_SCOPE;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CreateCollectionStatementTest {

    private Keyspace testKeySpace;
    private BucketOperator bucketOperator;
    private ClusterOperator clusterOperator;

    @BeforeEach
    void setUp() {
        clusterOperator = mock(ClusterOperator.class);
        bucketOperator = mock(BucketOperator.class);
        testKeySpace = keyspace(TEST_BUCKET, TEST_SCOPE, TEST_COLLECTION);
    }

    @Test
    void Should_not_createCollection_if_collection_exists_and_skip_is_true() {
        CreateCollectionStatement statement = new CreateCollectionStatement(testKeySpace, true);
        when(clusterOperator.getBucketOperator(TEST_BUCKET)).thenReturn(bucketOperator);
        when(bucketOperator.hasCollectionInScope(TEST_COLLECTION, TEST_SCOPE)).thenReturn(true);

        statement.execute(clusterOperator);

        verify(bucketOperator, never()).createCollection(TEST_COLLECTION, TEST_SCOPE);
    }

    @Test
    void Should_сreateCollection_if_it_not_exists_and_skip_is_false() {
        CreateCollectionStatement statement = new CreateCollectionStatement(testKeySpace, false);
        when(clusterOperator.getBucketOperator(TEST_BUCKET)).thenReturn(bucketOperator);
        when(bucketOperator.hasCollectionInScope(TEST_COLLECTION, TEST_SCOPE)).thenReturn(false);

        statement.execute(clusterOperator);

        verify(bucketOperator).createCollection(TEST_COLLECTION, TEST_SCOPE);
    }

    @Test
    void Should_fail_withException_if_collection_exists_and_skip_is_false() {
        CreateCollectionStatement statement = new CreateCollectionStatement(testKeySpace, false);
        when(clusterOperator.getBucketOperator(TEST_BUCKET)).thenReturn(bucketOperator);
        when(bucketOperator.hasCollectionInScope(TEST_COLLECTION, TEST_SCOPE)).thenReturn(true);
        doThrow(CollectionExistsException.class).when(bucketOperator).createCollection(TEST_COLLECTION, TEST_SCOPE);

        assertThatExceptionOfType(CollectionExistsException.class)
                .isThrownBy(() -> statement.execute(clusterOperator));
    }
}