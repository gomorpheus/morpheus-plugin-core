/**
 * a test widget
 * @author bdwheeler
 */
class LogCountWidget extends React.Component {
  
  constructor(props) {
    super(props);
    //filter config
    var searchOptions = { offset:0, max:10, stats:'true', timeframe:'today'};
    var searchFilter = '';
    //set timeframe
    Morpheus.timeService.applyTimeframeRange(searchOptions);
    //default state
    this.state = {
      loaded:false,
      autoRefresh:true,
      filter: searchFilter,
      options: searchOptions,
      data:null
    };
    //apply state config
    if(props.autoRefresh == false)
      this.state.autoRefresh = false;
    //bind methods
    this.setData = this.setData.bind(this);
    this.refreshData = this.refreshData.bind(this);
  }

  componentDidMount() {
    this.loadData();
    $(document).on('morpheus:refresh', this.refreshData);
  }

  //data methods
  refreshData() {
    if(this.state.autoRefresh == true)
      this.loadData();
  }

  loadData() {
    //call api for data...
    Morpheus.api.logs.search(this.state.filter, this.state.options).then(this.setData);
  }

  setData(results) {
    var searchFilter = results.config.filter;
    var searchOptions = results.config.options;
    //set it
    var newState = {};
    newState.data = results;
    newState.loaded = true;
    newState.date = Date.now();
    newState.error = false;
    newState.errorMessage = null;
    newState.filter = searchFilter;
    newState.options = searchOptions;
    //example data: {"sort":{"ts":"desc"},"offset":0,"start":"2021-11-15T17:47:04Z","end":"2021-11-22T17:47:04Z","max":450,"grandTotal":106,"total":106,"success":true,"count":106}
    //  "data":[
    //    {"typeCode":"ubuntu","level":"INFO","sourceType":"ubuntu","message":"Nov 22 17:43:10 bw-ubuntu-fusion-1 kernel: [    2.308066] sd 2:0:0:0: [sda] Cache data unavailable#\n",
    //      "ts":"2021-11-22T17:44:38Z","priority":300,"hostname":"localhost","logSignature":"a4369fe524a2b96cb48fa53c39160dfe440ec4647eaf5599968c9da71b391b09","objectId":"153240",
    //      "seq":2902,"_id":"bcdc6971-80bf-47db-8484-e07d797a71d4","signatureVerified":true}]
    //decorate the results - set the logo and timestamp data
    if(newState.data && newState.data.data) {
      //add the timestamp to the log data
      newState.data.date = newState.date;
      //iterate rows
      newState.data.data.forEach(function(row) {
        row.levelClass = 'level-' + row.level.toLowerCase();
        var logDate = Morpheus.timeService.getMoment(row.ts);
        row.timestamp = logDate.format('L hh:mm:ss A');
        row.logoText = '';
        if(row.typeCode == 'server') {
          row.logoText = row.sourceType;
        } else if(row.sourceType) {
          var typeLogo = Morpheus.LogoService.getLogoImage(row.sourceType);
          row.logoTitle = row.sourceType;
          if(typeLogo)
            row.logo = '/assets/containers/' + typeLogo + '.svg';
          else
            row.logoText = row.sourceType;
        } else {
          row.logo = '/assets/containers/appliance.svg'
          row.logoTitle = 'appliance'
        }
      });
      //set axis config
      if(searchOptions.startMs && searchOptions.endMs) {
        newState.data.axisConfig = Morpheus.timeService.getDateRangeAxis(searchOptions.startMs, searchOptions.endMs);
        newState.data.axisConfig.values = Morpheus.timeService.getDateRangeTickValues(newState.data.axisConfig.startDate, 
          newState.data.axisConfig.endDate, 6);
      }
    }
    //update the state
    this.setState(newState);
  }

  render() {
    var showChart = this.state.data && this.state.loaded == true;
    var emptyMessage = this.state.emptyMessage ? this.state.emptyMessage : Morpheus.utils.message('gomorpheus.label.noData');
    var chartConfig = {
      size: { height:140, width:520 },
      bar: { width: 9 }
    }
    return (
      <div className="widget-container widget-lg">
        <div id={'dashboard-widget-' + this.state.chartId} className="dashboard-widget">
          <div className="dashboard-widget-header">
            <svg className="icon"><use href="/assets/dashboard.svg#provisioning"></use></svg>
            <p>Log History</p>
          </div>
          <div className="dashboard-widget-body">
            <div className="log-view">
              <LogChart logData={this.state.data} loaded={this.state.loaded} config={chartConfig}/>
            </div>
            <div className={'widget-no-data' + (showChart ? ' hidden' : '')}>{emptyMessage}</div>
          </div>
        </div>
      </div>
    );
  }

}

//register it
Morpheus.components.register('logCountWidget', LogCountWidget);

$(document).ready(function () {
  const root = ReactDOM.createRoot(document.querySelector('#log-count-widget'));
  root.render(<LogCountWidget/>)
});


/*$(document).ready(function() {
  ReactDOM.render(
    <TestWidget/>,
    document.querySelector('#test-widget')
  );
});*/
