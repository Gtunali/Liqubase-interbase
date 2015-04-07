package liquibase.database.ext;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import liquibase.database.AbstractJdbcDatabase;
import liquibase.database.DatabaseConnection;
import liquibase.exception.DatabaseException;
import liquibase.snapshot.SnapshotGeneratorFactory;
import liquibase.snapshot.jvm.SequenceSnapshotGenerator;
import liquibase.snapshot.jvm.UniqueConstraintSnapshotGenerator;
import liquibase.snapshot.jvm.ext.SequenceSnapshotGeneratorInterbase;
import liquibase.snapshot.jvm.ext.UniqueConstraintSnapshotGeneratorInterbase;
import liquibase.structure.DatabaseObject;

public class InterbaseDatabase extends AbstractJdbcDatabase {

	public static final String PRODUCT_NAME = "INTERBASE";
	public static final Logger LOGGER = LoggerFactory.getLogger(InterbaseDatabase.class);
	private static Set<String> reservedWords = new HashSet<String>();
	
	
	public InterbaseDatabase() {
        super();

        super.setCurrentDateTimeFunction("CURRENT_TIMESTAMP");
        super.unquotedObjectsAreUppercased=false;
 
        try 
        {
        	super.setAutoCommit(true);
        } 
        catch (Exception e) 
        {
        	LOGGER.debug(e.getMessage());
        }
        SnapshotGeneratorFactory.getInstance().unregister(SequenceSnapshotGenerator.class);
        SequenceSnapshotGeneratorInterbase seqGen = new SequenceSnapshotGeneratorInterbase();
        SnapshotGeneratorFactory.getInstance().register(seqGen);
        
        SnapshotGeneratorFactory.getInstance().unregister(UniqueConstraintSnapshotGenerator.class);
        UniqueConstraintSnapshotGeneratorInterbase uniqueGen = new UniqueConstraintSnapshotGeneratorInterbase();
        SnapshotGeneratorFactory.getInstance().register(uniqueGen);
	}
	
	@Override
	public String getDefaultDriver(String url) {
		if (url.startsWith("jdbc:interbase:")) {
            return "interbase.interclient.Driver";
        }
        return null;
	}

	@Override
	public String getShortName() {
		return "interbase";
	}

	@Override
	public boolean isCorrectDatabaseImplementation(DatabaseConnection conn)
			throws DatabaseException {
		return PRODUCT_NAME.equalsIgnoreCase(conn.getDatabaseProductName());
	}

	@Override
    public boolean supportsInitiallyDeferrableColumns() {
        return false;
    }

    @Override
    public boolean supportsDDLInTransaction() {
        return false;
    }

    @Override
    public boolean isSystemObject(DatabaseObject example) {
        if (example instanceof liquibase.structure.core.Table && example.getName().startsWith("RDB$")) {
            return true;
        }
        return super.isSystemObject(example);    //To change body of overridden methods use File | Settings | File Templates.
    }
    
    @Override
    public String getSystemSchema() {
		return "";
	}
    
	@Override
	public boolean supportsTablespaces() {
		return false;
	}
	
	@Override
	public boolean supportsSchemas() {
		return false;
	}
	
	@Override
	public boolean supportsSequences() {
		return false;
	}

	@Override
	public int getPriority() {
		return PRIORITY_DEFAULT+1;
	}

	@Override
	protected String getDefaultDatabaseProductName() {
		return PRODUCT_NAME;
	}

	@Override
    public boolean isReservedWord(String string) {
        if (reservedWords.contains(string.toUpperCase())) {
            return true;
        }
        return super.isReservedWord(string);
    }
	
	@Override
    public String getAutoIncrementClause(final BigInteger startWith, final BigInteger incrementBy) {
        StringBuffer buffer = new StringBuffer();
        return buffer.toString();
    }
	
	
	private void setReservedWords() {
		reservedWords.addAll(Arrays.asList(
				"ADD",
				"ADMIN",
				"ALL",
				"ALTER",
				"AND",
				"ANY",
				"AS",
				"AT",
				"AVG",
				"BEGIN",
				"BETWEEN",
				"BIGINT",
				"BIT_LENGTH",
				"BLOB",
				"BOTH",
				"BY",
				"CASE",
				"CAST",
				"CHAR",
				"CHAR_LENGTH",
				"CHARACTER",
				"CHARACTER_LENGTH",
				"CHECK",
				"CLOSE",
				"COLLATE",
				"COLUMN",
				"COMMIT",
				"CONNECT",
				"CONSTRAINT",
				"COUNT",
				"CREATE",
				"CROSS",
				"CURRENT",
				"CURRENT_CONNECTION",
				"CURRENT_DATE",
				"CURRENT_ROLE",
				"CURRENT_TIME",
				"CURRENT_TIMESTAMP",
				"CURRENT_TRANSACTION",
				"CURRENT_USER",
				"CURSOR",
				"DATE",
				"DAY",
				"DEC",
				"DECIMAL",
				"DECLARE",
				"DEFAULT",
				"DELETE",
				"DISCONNECT",
				"DISTINCT",
				"DOUBLE",
				"DROP",
				"ELSE",
				"END",
				"ESCAPE",
				"EXECUTE",
				"EXISTS",
				"EXTERNAL",
				"EXTRACT",
				"FETCH",
				"FILTER",
				"FLOAT",
				"FOR",
				"FOREIGN",
				"FROM",
				"FULL",
				"FUNCTION",
				"GDSCODE",
				"GLOBAL",
				"GRANT",
				"GROUP",
				"HAVING",
				"HOUR",
				"IN",
				"INDEX",
				"INNER",
				"INSENSITIVE",
				"INSERT",
				"INT",
				"INTEGER",
				"INTO",
				"IS",
				"JOIN",
				"LEADING",
				"LEFT",
				"LIKE",
				"LONG",
				"LOWER",
				"MAX",
				"MAXIMUM_SEGMENT",
				"MERGE",
				"MIN",
				"MINUTE",
				"MONTH",
				"NATIONAL",
				"NATURAL",
				"NCHAR",
				"NO",
				"NOT",
				"NULL",
				"NUMERIC",
				"OCTET_LENGTH",
				"OF",
				"ON",
				"ONLY",
				"OPEN",
				"OR",
				"ORDER",
				"OUTER",
				"PARAMETER",
				"PLAN",
				"POSITION",
				"POST_EVENT",
				"PRECISION",
				"PRIMARY",
				"PROCEDURE",
				"RDB$DB_KEY",
				"REAL",
				"RECORD_VERSION",
				"RECREATE",
				"RECURSIVE",
				"REFERENCES",
				"RELEASE",
				"RETURNING_VALUES",
				"RETURNS",
				"REVOKE",
				"RIGHT",
				"ROLLBACK",
				"ROW_COUNT",
				"ROWS",
				"SAVEPOINT",
				"SECOND",
				"SELECT",
				"SENSITIVE",
				"SET",
				"SIMILAR",
				"SMALLINT",
				"SOME",
				"SQLCODE",
				"SQLSTATE",
				"START",
				"SUM",
				"TABLE",
				"THEN",
				"TIME",
				"TIMESTAMP",
				"TO",
				"TRAILING",
				"TRIGGER",
				"TRIM",
				"UNION",
				"UNIQUE",
				"UPDATE",
				"UPPER",
				"USER",
				"USING",
				"VALUE",
				"VALUES",
				"VARCHAR",
				"VARIABLE",
				"VARYING",
				"VIEW",
				"WHEN",
				"WHERE",
				"WHILE",
				"WITH",
				"YEAR"));
	}

	@Override
	public Integer getDefaultPort() {
		return 3050;
	}
}
