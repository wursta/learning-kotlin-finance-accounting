CREATE VERTEX TYPE card IF NOT EXISTS;
CREATE PROPERTY card.guid IF NOT EXISTS STRING (mandatory true, notnull true, readonly true);
CREATE PROPERTY card.number IF NOT EXISTS STRING (notnull true);
CREATE PROPERTY card.validFor IF NOT EXISTS STRING (notnull true);
CREATE PROPERTY card.owner IF NOT EXISTS STRING (notnull true);
CREATE PROPERTY card.bankGuid IF NOT EXISTS STRING (notnull true);
CREATE PROPERTY card.lockGuid IF NOT EXISTS STRING (mandatory true, notnull true);

CREATE INDEX IF NOT EXISTS ON card (guid) UNIQUE;

CREATE VERTEX TYPE expense IF NOT EXISTS;
CREATE PROPERTY expense.guid IF NOT EXISTS STRING (mandatory true, notnull true, readonly true);
CREATE PROPERTY expense.createDT IF NOT EXISTS LONG (notnull true);
CREATE PROPERTY expense.amount IF NOT EXISTS DOUBLE (notnull true, min 0);
CREATE PROPERTY expense.cardGuid IF NOT EXISTS STRING (notnull true);
CREATE PROPERTY expense.categoryGuid IF NOT EXISTS STRING (notnull true);
CREATE PROPERTY expense.lockGuid IF NOT EXISTS STRING (mandatory true, notnull true);

CREATE INDEX IF NOT EXISTS ON expense (guid) UNIQUE