/*
try {
  dbConnection = morpheus.report.getReadOnlyDatabaseConnection().blockingGet()
  if(reportResult.configMap?.phrase) {
    String phraseMatch = "${reportResult.configMap?.phrase}%"
    results = new Sql(dbConnection).rows("select avatar_file_name as Avatar,CONCAT(first_name, ' ', last_name) AS User, user.id AS UserId, count(*) as Count from audit_log INNER JOIN user ON COALESCE (audit_log.actual_user_id, audit_log.user_id) = user.id where description LIKE '%save|instance Created%' and description like ${phraseMatch} and user_id is not null and audit_log.date_created BETWEEN NOW() - INTERVAL 90 DAY AND NOW() GROUP BY userid ORDER BY count(*) DESC LIMIT 0,10;")
  } else {
    results = new Sql(dbConnection).rows("select avatar_file_name as Avatar,CONCAT(first_name, ' ', last_name) AS User, user.id AS UserId, count(*) as Count from audit_log INNER JOIN user ON COALESCE (audit_log.actual_user_id, audit_log.user_id) = user.id where description LIKE '%save|instance Created%' and user_id is not null and audit_log.date_created BETWEEN NOW() - INTERVAL 90 DAY AND NOW() GROUP BY userid ORDER BY count(*) DESC LIMIT 0,10;")
  }
  
  
  
  
  
select id, account_name, account_id, total_cost, total_price from account_invoice where total_cost != 0 and ref_type = "Account" and period = "202104" ORDER BY total_cost DESC LIMIT 0,10;

select period, SUM(total_cost) as cost from account_invoice where total_cost !=0 and ref_type = "Account" and ref_start BETWEEN NOW() - INTERVAL 365 DAY AND NOW() GROUP BY period ORDER BY cost DESC LIMIT 0,12;



void process(ReportResult reportResult) {
  morpheus.report.updateReportResultStatus(reportResult,ReportResult.Status.generating).blockingGet();
  Long displayOrder = 0
  List<GroovyRowResult> results = []
  Connection dbConnection
  
  try {
    dbConnection = morpheus.report.getReadOnlyDatabaseConnection().blockingGet()
      results = new Sql(dbConnection).rows("select id, account_name, account_id, total_cost, total_price from account_invoice where total_cost != 0 and ref_type = 'Account' and period = '202104' ORDER BY total_cost DESC LIMIT 0,10;")
      results << new Sql(dbConnection).rows("select period, SUM(total_cost) as allCost from account_invoice where total_cost !=0 and ref_type = 'Account' and ref_start BETWEEN NOW() - INTERVAL 365 DAY AND NOW() GROUP BY period ORDER BY allCost DESC LIMIT 0,12;")
  } finally {
    morpheus.report.releaseDatabaseConnection(dbConnection)
  }
  log.info("Results: ${results}")
  Observable<GroovyRowResult> observable = Observable.fromIterable(results) as Observable<GroovyRowResult>
  observable.map{ resultRow ->
    log.info("Mapping resultRow ${resultRow}")
    Map<String,Object> data = [id: resultRow.id, account_name: resultRow.account_name, account_id: resultRow.account_id, account_total_cost: resultRow.total_cost, account_total_price: resultRow.total_price, month: resultRow.period, allCosts: resultRow.allCost]
    ReportResultRow resultRowRecord = new ReportResultRow(section: ReportResultRow.SECTION_MAIN, displayOrder: displayOrder++, dataMap: data)
    log.info("resultRowRecord: ${resultRowRecord.dump()}")
    return resultRowRecord
  }.buffer(50).doOnComplete {
    morpheus.report.updateReportResultStatus(reportResult,ReportResult.Status.ready).blockingGet();
  }.doOnError { Throwable t ->
    morpheus.report.updateReportResultStatus(reportResult,ReportResult.Status.failed).blockingGet();
  }.subscribe {resultRows ->
    morpheus.report.appendResultRows(reportResult,resultRows).blockingGet()
  }
}
*/

https://demo.e-billexpress.com/ebpp/Perennials/Embed/Payment/SHO?Customer+Name=Jack+Baue&Order+%23+22-123456+%2f+Q-1234=Q24242&CFA+(Pending%2c+Waived%2c+or+Approved)=CFA+(Pending%2c+Waived%2c+or+Approved)&Amount+Due=2222.00&Due+Date=06%2f24%2f2024&Phone=2224442424&Email+Address=said.abdullah1%40wellsfargo.com&Address+1=1234+24th+St&Address+2=Suite+24&City=Washington&State=DC&Zip=22444&Country=USA

"Selection=%7B%7D&SkipAutoPay=&SkipDuplicate=&SkipFDPCheck=&SkipSupervisorApprovalCheck=&ApprovedByUserID=&SourcePage=Login&AddOnAmount=0&AddOnAccount=&AccountAddOnAmount=0&AccountAddOnAccountLabel=&IsPPD=False&UserlevelAddonSelection=&MailPaymentConfirmation=False&EmailPaymentConfirmation=False&MailConfirmationDelivery=&CVVCode="
