/**
 * user favorites
 * @author bdwheeler
 */
class UserFavoritesWidget extends React.Component {
  
  constructor(props) {
    super(props);
    //set state
    this.state = {
      loaded: false,
      autoRefresh: true,
      data: null
    };
    //dataType
    this.state.type = 'instance-favorite' //instance-user, app-favorite, app-user
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
    var apiQuery = '';
    var apiOptions = {max:5, favorites:'true'};
    switch(this.state.type) {
      case 'instance-favorite':
        Morpheus.api.instances.search(apiQuery, apiOptions).then(this.setData);    
        break;
    }
  }

  setData(results) {
    //set it
    var newState = {};
    newState.data = {};
    newState.data.config = results.config;
    newState.data.meta = results.meta;
    //set the data list
    newState.data.items = results.items;
    //mark it loaded
    newState.loaded = true;
    newState.date = Date.now();
    newState.error = false;
    newState.errorMessage = null;
    //update the state
    this.setState(newState);
  }

  render() {
    //setup
    var showTable = this.state.data && this.state.loaded == true;
    var itemList = this.state.data && this.state.data.items ? this.state.data.items : [];
    //var pillList = [
    //  {name:'Instances', value:'instance-favorite'},
    //  {name:'Apps', value:'app-favorite'},
    //  {name:'Clusters', value:'cluster-favorite'},
    //  {name:'Clouds', value:'cloud-favorite'},
    //  {name:'Workflows', value:'workflow-favorite'}
    //];
    //var currentPill = Morpheus.data.findMatchingItems(pillList, 'value', this.state.type);
    //<WidgetPills pills={pillList} defaultValue={this.state.type} align="center"/>
    //render
    return (
      <Widget>
        <WidgetHeader icon="/assets/featured.svg#Layer_1" title="Favorites"/>
        <div className="dashboard-widget-content">
          <table className="widget-table">
            <thead>
              <tr>
                <th className="col-lg">Instance</th>
                <th>Type</th>
                <th className="col-lg">Address</th>
              </tr>
            </thead>
            <tbody>
              { itemList.map(row => (
                <tr key={row.id}>
                  <td className="nowrap">
                    <ResourceLink type="instance" value={row.id} content={Morpheus.utils.clipLongText(row.name, 32)}/>
                  </td>
                  <td className="nowrap">{row.instanceType ? row.instanceType.name : ''}</td>
                  <td className="nowrap"><TableCellInstanceIpAddress data={row} showName={false} max={1}/></td>
                </tr>
              ))}
            </tbody>
          </table>
          <EmptyWidget isEmpty={showTable != true}/>
        </div>
      </Widget>
    );
  }

}

//register it
Morpheus.components.register('user-favorites-widget', UserFavoritesWidget);

$(document).ready(function () {
  const root = ReactDOM.createRoot(document.querySelector('#user-favorites-widget'));
  root.render(<UserFavoritesWidget/>)
});
