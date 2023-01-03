/**
 * a settings widget for the dashboard
 * @author aclement
 */
class SettingsWidget extends React.Component {

	constructor(props) {
		super(props);
		//set state
		this.state = {
			filters: props.filters,
			showFilters: false,
			showIcon: false
		};
		this.toggleFilters = this.toggleFilters.bind(this);
		this.toggleIcon = this.toggleIcon.bind(this);
		this.buildFilter = this.buildFilter.bind(this)
	}

	toggleFilters() {
		this.setState({showFilters: !this.state.showFilters})
	}

	toggleIcon() {
		this.setState({showIcon: !this.state.showIcon})
	}

	buildFilter(filterName, filterData) {
		if(filterData.type === "number") {
			return (<div key={filterName}>
				<label style={{paddingRight: "5px"}}
					   htmlFor={filterName}>{filterName.charAt(0).toUpperCase() + filterName.slice(1)}:</label>
				<input style={{width: "50px"}} name={filterName} pattern="[0-9]*" inputMode="numeric" type="text" defaultValue={filterData.currentValue}
					   onKeyUp={(x) => {
						   Morpheus.debounce(self,500, (y) => {
							   this.props.updateFilterValue(filterName, x.target.value)
							   this.state.filters[filterName].currentValue = x.target.value
						   });
					   }}/>
			</div>)
		}
	}

	render() {
		//setup
		let filters = ""
		if(this.state.filters) {
			const filterArray = Object.keys(this.state.filters)
			filters = filterArray.map((filter) => {
				return this.buildFilter(filter, this.state.filters[filter])
			})
		}
		const opacity = (this.state.showIcon || this.state.showFilters) ? "100%" : "40%";
		//render
		return (
			<div style={{position: "relative", zIndex: 10}}>
				{(
					<div style={{position: "absolute", top: "2px", right: "2px"}} onClick={this.toggleFilters} onMouseEnter={this.toggleIcon} onMouseLeave={this.toggleIcon}>
						<div style={{background: "#e6e8e9", borderRadius: "3px", padding: "1px 4px", opacity: opacity}}>
							<span style={{lineHeight:"inherit"}} className="glyphicon glyphicon-cog"></span>
						</div>
					</div>
				)}
				{(this.state.showFilters) && (
					<div style={{position: "absolute", top: "20px", right: "2px"}}>
						<div style={{background: "#e6e8e9", padding: "5px", borderRadius: "5px"}}>
							{filters}
						</div>
					</div>
				)}
			</div>
		);
	}
}

//register it
Morpheus.components.register('settings-widget', SettingsWidget);
