import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './timer.reducer';
import { ITimer } from 'app/shared/model/timer.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ITimerUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface ITimerUpdateState {
  isNew: boolean;
}

export class TimerUpdate extends React.Component<ITimerUpdateProps, ITimerUpdateState> {
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
    values.start = new Date(values.start);
    values.stop = new Date(values.stop);

    if (errors.length === 0) {
      const { timerEntity } = this.props;
      const entity = {
        ...timerEntity,
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
    this.props.history.goBack();
  };

  render() {
    const { timerEntity, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="rcraspiApp.timer.home.createOrEditLabel">
              <Translate contentKey="rcraspiApp.timer.home.createOrEditLabel">Create or edit a Timer</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : timerEntity} onSubmit={this.saveEntity}>
                <AvGroup>
                  <Label id="nameLabel" for="name">
                    <Translate contentKey="rcraspiApp.timer.name">Name</Translate>
                  </Label>
                  <AvField
                    id="timer-name"
                    type="text"
                    name="name"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="startLabel" for="start">
                    <Translate contentKey="rcraspiApp.timer.start">Start</Translate>
                  </Label>
                  <AvInput
                    id="timer-start"
                    type="datetime-local"
                    className="form-control"
                    name="start"
                    value={isNew ? null : convertDateTimeFromServer(this.props.timerEntity.start)}
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="stopLabel" for="stop">
                    <Translate contentKey="rcraspiApp.timer.stop">Stop</Translate>
                  </Label>
                  <AvInput
                    id="timer-stop"
                    type="datetime-local"
                    className="form-control"
                    name="stop"
                    value={isNew ? null : convertDateTimeFromServer(this.props.timerEntity.stop)}
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="repeatLabel">
                    <Translate contentKey="rcraspiApp.timer.repeat">Repeat</Translate>
                  </Label>
                  <AvInput
                    id="timer-repeat"
                    type="select"
                    className="form-control"
                    name="repeat"
                    value={(!isNew && timerEntity.repeat) || 'ONCE'}
                  >
                    <option value="ONCE">
                      <Translate contentKey="rcraspiApp.Repeat.ONCE" />
                    </option>
                    { /*
                    <option value="DAY">
                      <Translate contentKey="rcraspiApp.Repeat.DAY" />
                    </option>
                    <option value="WEEK">
                      <Translate contentKey="rcraspiApp.Repeat.WEEK" />
                    </option>
                    <option value="MONTH">
                      <Translate contentKey="rcraspiApp.Repeat.MONTH" />
                    </option>
                    <option value="YEAR">
                      <Translate contentKey="rcraspiApp.Repeat.YEAR" />
                    </option>
                    */}
                  </AvInput>
                </AvGroup>
                <Button onClick={this.handleClose} id="cancel-save" replace color="info">
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
  timerEntity: storeState.timer.entity,
  loading: storeState.timer.loading,
  updating: storeState.timer.updating,
  updateSuccess: storeState.timer.updateSuccess
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
)(TimerUpdate);
