/**
 counter widget that loads data
 * @author bdwheeler
 */
class LogTrendsWidget extends React.Component {

  constructor(props) {
    super(props);
    //set state
    this.state = {
      loaded: false,
      autoRefresh: true,
      data: null
    };
    //dataType
    this.state.type = 'all' //error, warnings, all?
    //apply state config
    if(props.autoRefresh == false)
      this.state.autoRefresh = false;
    //bind methods
    this.setData = this.setData.bind(this);
    this.refreshData = this.refreshData.bind(this);
    this.onPillChange = this.onPillChange.bind(this);
  }

  componentDidMount() {
    this.loadData();
    $(document).on('morpheus:refresh', this.refreshData);
  }

  onPillChange(value) {
    if(this.state.type != value) {
      var newState = {};
      newState.type = value;
      this.setState(newState, this.loadData);
    }
  }

  //data methods
  refreshData() {
    if(this.state.autoRefresh == true)
      this.loadData();
  }

  loadData() {
    //call api for data...
    var apiQuery;
    var apiOptions = { minDocCount:1, max:5, level:'' };
    switch(this.state.type) {
      case 'warning':
        apiOptions.level = 'WARNING';
        break;
      case 'error':
        apiOptions.level = 'ERROR';
        break;
      case 'all':
      default:
        apiOptions.level = '';
        break;
    }
    Morpheus.api.logs.trends(apiQuery, apiOptions).then(this.setData);
  }

  setData(results) {
    //set it
    var newState = {};
    newState.data = {};
    newState.data.config = results.config;
    newState.data.offset = results.offset;
    newState.data.max = results.max;
    //set the data list
    newState.data.items = results.data;
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
    var isLoaded = this.state.data && this.state.data.loaded == true;
    var itemList = isLoaded == true && this.state.data.items ? this.state.data.items : [];
    var showTable = isLoaded == true && itemList.length > 0;
    //pills
    var pillList = [
      {name:'All', value:'all'},
      {name:'Errors', value:'error'},
      {name:'Warnings', value:'warning'}
    ];
    //render
    return (
      <Widget>
        <WidgetHeader icon="/assets/dashboard.svg#logs" title="Log trends" link="/monitoring/logs"/>
        <WidgetPills pills={pillList} defaultValue={this.state.type} align="center" onPillChange={this.onPillChange}/>
        <div className="dashboard-widget-content">
          <table className={'widget-table' + (showTable ? '' : ' hidden')}>
            <tbody>
              { itemList.map(row => (
                <tr key={Morpheus.utils.simpleHash(row.message)}>
                  <td className="nowrap">{row.message ? Morpheus.utils.slice(row.message, 85) : ''}</td>
                </tr>
              ))}
            </tbody>
          </table>
          <EmptyWidget isEmpty={isLoaded == true && showTable != true}/>
          <LoadingWidget isLoading={isLoaded != true}/>
        </div>
      </Widget>
    );
  }

}

//register it
Morpheus.components.register('log-trends-widget', LogTrendsWidget);

$(document).ready(function () {
  const root = ReactDOM.createRoot(document.querySelector('#log-trends-widget'));
  root.render(<LogTrendsWidget/>)
});
