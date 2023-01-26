/**
 * instance count by status
 * @author bdwheeler
 */
class EnvironmentCountWidget extends React.Component {
  
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
    this.loadData = this.loadData.bind(this);
    this.setData = this.setData.bind(this);
    this.refreshData = this.refreshData.bind(this);
  }

  componentDidMount() {
    //load the data
    this.loadData();
    //configure auto refresh
    $(document).on('morpheus:refresh', this.refreshData);
  }

  //data methods
  refreshData() {
    if(this.state.autoRefresh == true)
      this.loadData();
  }

  loadData() {
    var now = new Date();
    var self = this;
    //call api for data..
    Morpheus.api.health.counts().then(this.setData);
  }

  setData(results) {
    var newState = {};
    newState.data = {};
    //set the data list
    newState.data.success = results.success;
    newState.data.groups = results.groups ? results.groups : 0;
    newState.data.clouds = results.clouds ? results.clouds : 0;
    newState.data.clusters = results.clusters ? results.clusters : 0;
    newState.data.instances = results.instances ? results.instances : 0;
    newState.data.apps = results.apps ? results.apps : 0;
    newState.data.resources = results.resources ? results.resources : 0;
    newState.data.users = results.users ? results.users : 0;
    //set loaded
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
    var showChart = isLoaded == true && this.state.data.success == true;
    var countData = this.state.data ? this.state.data : {};
    //render
    return (
      <Widget>
        <WidgetHeader icon="/assets/dashboard.svg#provisioning" title="Environment"/>
        <div className={'dashboard-widget-content' + (showChart ? '' : ' hidden')}>
          <div className="row">
            <div className="col-sm-2 dashboard-widget-count count-rows">
              <span className="count-value">{countData.groups}</span>
              <span className="count-label">{Morpheus.utils.message('gomorpheus.label.groups')}</span>
            </div>
            <div className="col-sm-2 dashboard-widget-count count-rows">
              <span className="count-value">{countData.clouds}</span>
              <span className="count-label">{Morpheus.utils.message('gomorpheus.label.clouds')}</span>
            </div>
            <div className="col-sm-2 dashboard-widget-count count-rows">
              <span className="count-value">{countData.clusters}</span>
              <span className="count-label">{Morpheus.utils.message('gomorpheus.label.clusters')}</span>
            </div>
            <div className="col-sm-2 dashboard-widget-count count-rows">
              <span className="count-value">{countData.apps}</span>
              <span className="count-label">{Morpheus.utils.message('gomorpheus.label.apps')}</span>
            </div>
            <div className="col-sm-2 dashboard-widget-count count-rows">
              <span className="count-value">{countData.instances}</span>
              <span className="count-label">{Morpheus.utils.message('gomorpheus.label.instances')}</span>
            </div>
            <div className="col-sm-2 dashboard-widget-count count-rows">
              <span className="count-value">{countData.users}</span>
              <span className="count-label">{Morpheus.utils.message('gomorpheus.label.users')}</span>
            </div>
          </div>
        </div>
        <EmptyWidget isEmpty={isLoaded == true && showChart != true}/>
        <LoadingWidget isLoading={isLoaded != true}/>
      </Widget>
    );
  }

}

//register it
Morpheus.components.register('environment-count-widget', EnvironmentCountWidget);

$(document).ready(function () {
	const root = ReactDOM.createRoot(document.querySelector('#environment-count-widget'));
	root.render(<EnvironmentCountWidget/>)
});
