package liquibase.snapshot.jvm.ext;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import liquibase.database.Database;
import liquibase.database.ext.InterbaseDatabase;
import liquibase.exception.DatabaseException;
import liquibase.snapshot.DatabaseSnapshot;
import liquibase.snapshot.InvalidExampleException;
import liquibase.snapshot.SnapshotGeneratorChain;
import liquibase.snapshot.jvm.SequenceSnapshotGenerator;
import liquibase.statement.core.RenameColumnStatement;
import liquibase.structure.DatabaseObject;
import liquibase.structure.core.Schema;

public class SequenceSnapshotGeneratorInterbase extends
		SequenceSnapshotGenerator {
	
	Logger LOGGER = LoggerFactory.getLogger(SequenceSnapshotGeneratorInterbase.class);
	
    @Override
	protected String getSelectSequenceSql(Schema schema, Database database) {
    	if (database instanceof InterbaseDatabase) 
    	{
    		return "SELECT RDB$GENERATOR_NAME AS SEQUENCE_NAME FROM RDB$GENERATORS WHERE RDB$SYSTEM_FLAG IS NULL OR RDB$SYSTEM_FLAG = 0";
    	} else {
            throw new liquibase.exception.UnexpectedLiquibaseException("Interbase -- Don't know how to query for sequences on " + database);
        }
    } 

}
