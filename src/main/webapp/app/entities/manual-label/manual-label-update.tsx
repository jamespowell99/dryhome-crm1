import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './manual-label.reducer';
import { IManualLabel } from 'app/shared/model/manual-label.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IManualLabelUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IManualLabelUpdateState {
  isNew: boolean;
}

export class ManualLabelUpdate extends React.Component<IManualLabelUpdateProps, IManualLabelUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      isNew: !this.props.match.params || !this.props.match.params.id
    };
  }

  componentWillUpdate(nextProps, nextState) {
    if (nextProps.updateSuccess !== this.props.updateSuccess && nextProps.updateSuccess) {
      this.handleClose();
    }
  }

  componentDidMount() {
    if (this.state.isNew) {
      this.props.reset();
    } else {
      this.props.getEntity(this.props.match.params.id);
    }
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { manualLabelEntity } = this.props;
      const entity = {
        ...manualLabelEntity,
        ...values
      };

      if (this.state.isNew) {
        this.props.createEntity(entity);
      } else {
        this.props.updateEntity(entity);
      }
    }
  };

  handleClose = () => {
    this.props.history.push('/entity/manual-label');
  };

  render() {
    const { manualLabelEntity, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="dryhomecrm1App.manualLabel.home.createOrEditLabel">Create or edit a ManualLabel</h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : manualLabelEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">ID</Label>
                    <AvInput id="manual-label-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="line1Label" for="line1">
                    Line 1
                  </Label>
                  <AvField
                    id="manual-label-line1"
                    type="text"
                    name="line1"
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="line2Label" for="line2">
                    Line 2
                  </Label>
                  <AvField id="manual-label-line2" type="text" name="line2" />
                </AvGroup>
                <AvGroup>
                  <Label id="line3Label" for="line3">
                    Line 3
                  </Label>
                  <AvField id="manual-label-line3" type="text" name="line3" />
                </AvGroup>
                <AvGroup>
                  <Label id="line4Label" for="line4">
                    Line 4
                  </Label>
                  <AvField id="manual-label-line4" type="text" name="line4" />
                </AvGroup>
                <AvGroup>
                  <Label id="line5Label" for="line5">
                    Line 5
                  </Label>
                  <AvField id="manual-label-line5" type="text" name="line5" />
                </AvGroup>
                <AvGroup>
                  <Label id="line6Label" for="line6">
                    Line 6
                  </Label>
                  <AvField id="manual-label-line6" type="text" name="line6" />
                </AvGroup>
                <AvGroup>
                  <Label id="line7Label" for="line7">
                    Line 7
                  </Label>
                  <AvField id="manual-label-line7" type="text" name="line7" />
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/manual-label" replace color="info">
                  <FontAwesomeIcon icon="arrow-left" />
                  &nbsp;
                  <span className="d-none d-md-inline">Back</span>
                </Button>
                &nbsp;
                <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                  <FontAwesomeIcon icon="save" />
                  &nbsp; Save
                </Button>
              </AvForm>
            )}
          </Col>
        </Row>
      </div>
    );
  }
}

const mapStateToProps = (storeState: IRootState) => ({
  manualLabelEntity: storeState.manualLabel.entity,
  loading: storeState.manualLabel.loading,
  updating: storeState.manualLabel.updating,
  updateSuccess: storeState.manualLabel.updateSuccess
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ManualLabelUpdate);
