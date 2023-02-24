/**
 * alarm counts and open alarm table
 * @author bdwheeler
 */
class CurrentAlarmsWidget extends React.Component {
  
  constructor(props) {
    super(props);
    //set state
    this.state = {
      loaded: false,
      autoRefresh: true,
      data: null
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
    var self = this;
    //call api for stats data
    Morpheus.api.health.alarmStats().then(function(statsResults) {
      //call for open alarms
      var apiQuery = '';//'active = true and acknowledged != true';
      var apiOptions = { alarmStatus:'open', sort:'status,startDate', order:'asc,desc', max:5};
      Morpheus.api.health.alarmSearch(apiQuery, apiOptions).then(function(alarmResults) {
        self.setData(statsResults, alarmResults);
      });

    });
  }

  setData(statsResults, alarmResults) {
    //set it
    var newState = {};
    newState.data = {};
    newState.data.stats = statsResults.stats;
    //aggregate data for display
    newState.data.error = 0;
    newState.data.warning = 0;
    //if we have values
    if(statsResults.stats) {
      if(statsResults.stats.error)
        newState.data.error = statsResults.stats.error;
      if(statsResults.stats.warning)
        newState.data.warning = statsResults.stats.warning;
    }
    if(alarmResults.items) {
      newState.data.alarmList = alarmResults.items;
    }
    //mark it loaded
    newState.loaded = true;
    newState.data.loaded = true;
    newState.date = Date.now();
    newState.error = false;
    newState.errorMessage = null;
    //update the state
    this.setState(newState);
  }

  render() {
    //setup
    var showChart = this.state.data && this.state.loaded == true;
    var countValue = '';
    var countText = '';
    var countColor = '';
    if(showChart == true ) {
      if(this.state.data.error > 0) {
        countValue = this.state.data.error;
        countColor = ' text-danger';
        if(this.state.data.error > 1)
          countText = Morpheus.utils.message('gomorpheus.label.errors');
        else
          countText = Morpheus.utils.message('gomorpheus.label.error');
      } else if(this.state.data.warning > 0) {
        countValue = this.state.data.warning;
        countColor = ' text-warning';
        if(this.state.data.warning > 1)
          countText = Morpheus.utils.message('gomorpheus.label.warnings');
        else
          countText = Morpheus.utils.message('gomorpheus.label.warning');
      } else {
        countValue = '0';
      }
    }
    var alarmList = (showChart && this.state.data.alarmList) ? this.state.data.alarmList : [];
    //render
    return (
      <Widget>
        <WidgetHeader icon="/assets/dashboard.svg#alert" title={Morpheus.utils.message('gomorpheus.label.alarms')} link="/operations/alarms"/>
        <div>
          <div className={'dashboard-widget-chart-count' + (showChart ? '' : ' hidden')}>
            <span className={'count-value' + countColor}>{countValue}</span>
            <span className='count-label'>{countText}</span>
          </div>
          <div className={'dashboard-widget-chart-table' + (showChart ? '' : ' hidden')}>
            <table className="widget-table">
              <thead>
                <tr>
                  <th className="col-lg">Resource</th>
                  <th className="col-lg">Info</th>
                  <th>Duration</th>
                </tr>
              </thead>
              <tbody>
                { alarmList.map(row => (
                  <tr data-id={row.id} key={row.id}>
                    <td className="nowrap">
                      <ResourceLink type={row.refType} value={row.refId} content={Morpheus.utils.clipLongText((row.resourceName ? row.resourceName : row.refName), 26)}/>
                    </td>
                    <td className="nowrap">{Morpheus.utils.clipLongText(row.name, 26)}</td>
                    <td className={'text-right ' + (Morpheus.utils.statusTextClass(row.status))}>{Morpheus.data.formatDuration(row.startDate)}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      </Widget>
    );
  }

}

//register it
Morpheus.components.register('current-alarms-widget', CurrentAlarmsWidget);

$(document).ready(function () {
	const root = ReactDOM.createRoot(document.querySelector('#current-alarms-widget'));
	root.render(<CurrentAlarmsWidget/>)
});
