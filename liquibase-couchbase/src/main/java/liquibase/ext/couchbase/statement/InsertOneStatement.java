package liquibase.ext.couchbase.statement;


import com.wdt.couchbase.Keyspace;

import liquibase.ext.couchbase.database.CouchbaseConnection;
import liquibase.ext.couchbase.types.Document;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import static java.util.Collections.singletonList;

@Getter
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class InsertOneStatement extends CouchbaseStatement {
    private final Keyspace keyspace;
    private final Document document;

    @Override
    public void execute(CouchbaseConnection connection) {
        InsertManyStatement statement = new InsertManyStatement(keyspace, singletonList(document));
        statement.execute(connection);
    }
}
