/**
 counter widget that loads data
 * @author bdwheeler
 */
class BackupStatsWidget extends React.Component {

	constructor(props) {
		super(props);
		//set state
		this.state = {
			loaded: false,
			autoRefresh: true,
			data: null,
			days: 1,
			widgetId: Morpheus.utils.generateGuid(),
			showSettings: false
		};
		//apply state config
		if (props.autoRefresh == false)
			this.state.autoRefresh = false;
		//bind methods
		this.setData = this.setData.bind(this);
		this.refreshData = this.refreshData.bind(this);
		this.updateFilterValue = this.updateFilterValue.bind(this);
	}

	componentDidMount() {
		this.loadData();
		$(document).on('morpheus:refresh', this.refreshData);
	}

	//data methods
	refreshData() {
		if (this.state.autoRefresh == true)
			this.loadData();
	}

	loadData() {
		//call api for data...
		var apiFilter;
		var apiOptions = {};
		Morpheus.api.backups.count('', {numDays: this.state.days}).then(this.setData)
	}


	updateFilterValue(fieldName, newValue) {
		if(fieldName && newValue) {
			const newState = {};
			newState[fieldName] = newValue;
			this.setState(newState);
		}
	}

	setData(results) {
		//set it
		var newState = {};
		newState.data = {};
		newState.data.config = results.config;
		//set the data list
		//aggregate data for display
		newState.data.successful = results.data.successCount
		newState.data.failed = results.data.failedCount
		newState.data.totalResuts = results.data.total
		newState.data.successPercent = 0
		newState.data.failPercent = 0
		if(newState.data.totalResuts && newState.data.totalResuts != 0) {
			newState.data.successPercent = newState.data.successful / newState.data.totalResuts * 100
			newState.data.failPercent = 100 - newState.data.successPercent
		}
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
		const showChart = this.state.data && this.state.loaded == true;
		var widgetTitle = 'Backups ' + this.state.days + (this.state.days == 1 ? 'Day' : 'Days');
		//data
		const successfulLabel = Morpheus.utils.message('gomorpheus.successful');
		const failedLabel = Morpheus.utils.message('gomorpheus.failed');
		const successfulCount = this.state.data?.successful ? this.state.data.successful : 0;
		const failedCount = this.state.data?.failed ? this.state.data.failed : 0;
		const successPercent = this.state.data?.successPercent ? this.state.data.successPercent : 0;
		const failPercent = this.state.data?.failPercent ? this.state.data.failPercent : 0;
		const filters = {
			update: this.updateFilterValue,
			opts: [
				{ label:'Days',
					fieldName:'days',
					value: this.state.days,
					type: "number",
					min: 0,
					max: 30
				}
			]
		}
		//render
		return (
			<Widget settings={filters}>
				<WidgetHeader icon="/assets/dashboard.svg#backup" title={widgetTitle}/>
				<div id={'backup-stats-chart-' + this.state.widgetId}
						className={'line-chart-widget' + (showChart ? '' : ' hidden')}
						style={{position:'relative', marginTop:'10px'}}>
					<div style={{fontSize:'15px', marginLeft:'7px', marginRight:'12px'}}>
						<span>{successfulLabel}</span><span style={{float:'right'}}>{successfulCount}</span>
					</div>
					<div id="backup-success-line" className="backup-success-line"
							style={{backgroundColor:'#e7e7e6', height:'2px'}}>
						<svg width={successPercent + '%'} height="3px" style={{top:'-12px', position:'relative'}}>
							<line x2="100%" y2="0" strokeWidth="6" stroke={Morph.chartConfigs.colors.green}></line>
						</svg>
					</div>
					<div style={{fontSize:'15px', marginLeft:'7px', marginRight:'12px', marginTop:'20px'}}>
						<span>{failedLabel}</span><span style={{float:'right'}}>{failedCount}</span>
					</div>
					<div id="backup-fail-line" className="backup-fail-line"
							style={{backgroundColor:'#e7e7e6', height:'2px'}}>
						<svg width={failPercent + '%'} height="3px" style={{top:'-12px', position:'relative'}}>
							<line x2="100%" y2="0" strokeWidth="6" stroke={Morph.chartConfigs.colors.red}></line>
						</svg>
					</div>
				</div>
			</Widget>
		);
	}

}

//register it
Morpheus.components.register('backup-stats-widget', BackupStatsWidget);

$(document).ready(function () {
	const root = ReactDOM.createRoot(document.querySelector('#backup-stats-widget'));
	root.render(<BackupStatsWidget/>)
});
