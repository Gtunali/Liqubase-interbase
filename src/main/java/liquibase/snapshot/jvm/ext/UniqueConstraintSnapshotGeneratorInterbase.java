package liquibase.snapshot.jvm.ext;

import liquibase.executor.ExecutorService;
import liquibase.structure.core.UniqueConstraint;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import liquibase.database.AbstractJdbcDatabase;
import liquibase.database.Database;
import liquibase.database.ext.InterbaseDatabase;
import liquibase.exception.DatabaseException;
import liquibase.snapshot.DatabaseSnapshot;
import liquibase.snapshot.CachedRow;
import liquibase.snapshot.InvalidExampleException;
import liquibase.snapshot.JdbcDatabaseSnapshot;
import liquibase.snapshot.SnapshotGeneratorChain;
import liquibase.snapshot.jvm.UniqueConstraintSnapshotGenerator;
import liquibase.statement.core.RawSqlStatement;
import liquibase.statement.core.RenameColumnStatement;
import liquibase.structure.DatabaseObject;
import liquibase.structure.core.*;

public class UniqueConstraintSnapshotGeneratorInterbase extends UniqueConstraintSnapshotGenerator {
	
	Logger LOGGER = LoggerFactory.getLogger(UniqueConstraintSnapshotGeneratorInterbase.class);
	
    @Override
    protected List<Map<String, ?>> listColumns(UniqueConstraint example, Database database) throws DatabaseException {
        String name = example.getName();
    	
    	String sql = null;
    	sql = "SELECT RDB$INDEX_SEGMENTS.RDB$FIELD_NAME AS column_name " +
                "FROM RDB$INDEX_SEGMENTS " +
                "LEFT JOIN RDB$INDICES ON RDB$INDICES.RDB$INDEX_NAME = RDB$INDEX_SEGMENTS.RDB$INDEX_NAME " +
                "WHERE UPPER(RDB$INDICES.RDB$INDEX_NAME)='"+database.correctObjectName(name, UniqueConstraint.class)+"' " +
                "ORDER BY RDB$INDEX_SEGMENTS.RDB$FIELD_POSITION";
    	return ExecutorService.getInstance().getExecutor(database).queryForList(new RawSqlStatement(sql));
    }

}
