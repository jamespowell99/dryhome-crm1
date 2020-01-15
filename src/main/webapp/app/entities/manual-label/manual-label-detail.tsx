import React from 'react';
import axios from 'axios';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, ButtonDropdown, DropdownItem, DropdownMenu, DropdownToggle } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './manual-label.reducer';
import { IManualLabel } from 'app/shared/model/manual-label.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IManualLabelDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IManualLabelDetailState {
  dropdownOpen: boolean;
}

export class ManualLabelDetail extends React.Component<IManualLabelDetailProps, IManualLabelDetailState> {
  constructor(props) {
    super(props);

    this.toggle = this.toggle.bind(this);
    this.state = {
      dropdownOpen: false
    };
  }

  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  callDocument = event => {
    const { manualLabelEntity } = this.props;
    const docName = event.target.id;
    axios({
      url: `api/manual-labels/${manualLabelEntity.id}/document?documentName=${docName}`,
      method: 'GET',
      responseType: 'blob' // important
    }).then(response => {
      const url = window.URL.createObjectURL(new Blob([response.data]));
      const link = document.createElement('a');
      link.href = url;
      const currentDate = new Date();
      const currentDateAsString = `${currentDate.getFullYear()}${currentDate.getMonth() + 1}${currentDate.getDate()}`;
      const currentTimeAsString = `${currentDate.getHours()}${currentDate.getMinutes()}${currentDate.getSeconds()}`;
      const currentDateToUse = `${currentDateAsString}${currentTimeAsString}`;
      link.setAttribute('download', `manual-label-${manualLabelEntity.id}-${docName}-${currentDateToUse}.docx`);
      document.body.appendChild(link);
      link.click();
    });
    // console.log('done');
  };

  toggle() {
    this.setState({
      dropdownOpen: !this.state.dropdownOpen
    });
  }

  render() {
    const { manualLabelEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            ManualLabel [<b>{manualLabelEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="line1">Line 1</span>
            </dt>
            <dd>{manualLabelEntity.line1}</dd>
            <dt>
              <span id="line2">Line 2</span>
            </dt>
            <dd>{manualLabelEntity.line2}</dd>
            <dt>
              <span id="line3">Line 3</span>
            </dt>
            <dd>{manualLabelEntity.line3}</dd>
            <dt>
              <span id="line4">Line 4</span>
            </dt>
            <dd>{manualLabelEntity.line4}</dd>
            <dt>
              <span id="line5">Line 5</span>
            </dt>
            <dd>{manualLabelEntity.line5}</dd>
            <dt>
              <span id="line6">Line 6</span>
            </dt>
            <dd>{manualLabelEntity.line6}</dd>
            <dt>
              <span id="line7">Line 7</span>
            </dt>
            <dd>{manualLabelEntity.line7}</dd>
          </dl>
          <Button tag={Link} to="/entity/manual-label" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/manual-label/${manualLabelEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
          <ButtonDropdown isOpen={this.state.dropdownOpen} toggle={this.toggle}>
            <DropdownToggle caret color="primary">
              Documents
            </DropdownToggle>
            <DropdownMenu>
              <DropdownItem onClick={this.callDocument} id={'labels'}>
                Labels
              </DropdownItem>
            </DropdownMenu>
          </ButtonDropdown>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ manualLabel }: IRootState) => ({
  manualLabelEntity: manualLabel.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ManualLabelDetail);
