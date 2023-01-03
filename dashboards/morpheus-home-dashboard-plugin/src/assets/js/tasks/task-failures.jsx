/**
 * a list of task failures
 * @author bdwheeler
 */
class TaskFailWidget extends React.Component {
  
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
    Morpheus.api.executions.tasks.search('',{status:'failed'}).then(this.setData)
  }

  setData(results) {
    let data = results.data.map(result => {
      let name = result.execution.name
      let link = result.execution.link
      let message = result.process.message
      let startDate = moment(result.process.startDate).format("L") + ' ' + moment(result.process.startDate).format("LT")
      let target = result.process.targetName? result.process.targetName : Morpheus.utils.message('gomorpheus.label.none')
      return {name: name, link:link, message:message, id: result.process.id, startDate: startDate , target: target}
    })
    //update the state
    this.setState({data: data, loaded: true});
  }

  isPillActive(type) {
    return this.state.type === type
  }
  setActive(type) {
    return function() {
      this.setState({type:type})
    }.bind(this)
  }
  renderTable() {
    
    
  }

  render() {
    //setup
    var itemList = this.state.data || [];
    var showTable = this.state.data && this.state.loaded == true;
    //render
    return (
      <Widget scrollBody={true}>
        <WidgetHeader icon="/assets/dashboard.svg#provisioning" title="Task Failures"/>
        <table className="widget-table">
          <thead>
            <tr>
              <th>Task</th>
              <th>Message</th>
              <th>Target</th>
              <th>Date</th>
            </tr>
          </thead>
          <tbody>
            { itemList.map(row => (
              <tr key={row.id}>
                <td><a href={row.link}>{row.name}</a></td>
                <td>{row.message}</td>
                <td>{row.target}</td>
                <td>{row.startDate}</td>
              </tr>
            ))}
          </tbody>
        </table>
        <LoadingWidget isLoading={!this.state.loaded}/>
      </Widget>
    );
  }

}

//register it
Morpheus.components.register('task-fail-widget', TaskFailWidget);

$(document).ready(function () {
  const root = ReactDOM.createRoot(document.querySelector('#dashboard-item-task-failures'));
  root.render(<TaskFailWidget/>)
});
