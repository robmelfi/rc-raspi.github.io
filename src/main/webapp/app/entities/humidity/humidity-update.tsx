import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './humidity.reducer';
import { IHumidity } from 'app/shared/model/humidity.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IHumidityUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IHumidityUpdateState {
  isNew: boolean;
}

export class HumidityUpdate extends React.Component<IHumidityUpdateProps, IHumidityUpdateState> {
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
    if (!this.state.isNew) {
      this.props.getEntity(this.props.match.params.id);
    }
  }

  saveEntity = (event, errors, values) => {
    values.timestamp = new Date(values.timestamp);

    if (errors.length === 0) {
      const { humidityEntity } = this.props;
      const entity = {
        ...humidityEntity,
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
    this.props.history.push('/entity/humidity');
  };

  render() {
    const { humidityEntity, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="rcraspiApp.humidity.home.createOrEditLabel">
              <Translate contentKey="rcraspiApp.humidity.home.createOrEditLabel">Create or edit a Humidity</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : humidityEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="humidity-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="valueLabel" for="value">
                    <Translate contentKey="rcraspiApp.humidity.value">Value</Translate>
                  </Label>
                  <AvField id="humidity-value" type="string" className="form-control" name="value" />
                </AvGroup>
                <AvGroup>
                  <Label id="timestampLabel" for="timestamp">
                    <Translate contentKey="rcraspiApp.humidity.timestamp">Timestamp</Translate>
                  </Label>
                  <AvInput
                    id="humidity-timestamp"
                    type="datetime-local"
                    className="form-control"
                    name="timestamp"
                    value={isNew ? null : convertDateTimeFromServer(this.props.humidityEntity.timestamp)}
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/humidity" replace color="info">
                  <FontAwesomeIcon icon="arrow-left" />&nbsp;
                  <span className="d-none d-md-inline">
                    <Translate contentKey="entity.action.back">Back</Translate>
                  </span>
                </Button>
                &nbsp;
                <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                  <FontAwesomeIcon icon="save" />&nbsp;
                  <Translate contentKey="entity.action.save">Save</Translate>
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
  humidityEntity: storeState.humidity.entity,
  loading: storeState.humidity.loading,
  updating: storeState.humidity.updating,
  updateSuccess: storeState.humidity.updateSuccess
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
)(HumidityUpdate);
