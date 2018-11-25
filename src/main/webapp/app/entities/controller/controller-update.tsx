import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label, FormText } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IPin } from 'app/shared/model/pin.model';
import { getEntities as getPins } from 'app/entities/pin/pin.reducer';
import { getEntity, updateEntity, createEntity, reset } from './controller.reducer';
import { IController } from 'app/shared/model/controller.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IControllerUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IControllerUpdateState {
  isNew: boolean;
  pinId: string;
}

export class ControllerUpdate extends React.Component<IControllerUpdateProps, IControllerUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      pinId: '0',
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

    this.props.getPins();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { controllerEntity } = this.props;
      const entity = {
        ...controllerEntity,
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
    this.props.history.push('/entity/controller');
  };

  render() {
    const { controllerEntity, pins, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="rcraspiApp.controller.home.createOrEditLabel">
              <Translate contentKey="rcraspiApp.controller.home.createOrEditLabel">Create or edit a Controller</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : controllerEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="controller-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="nameLabel" for="name">
                    <Translate contentKey="rcraspiApp.controller.name">Name</Translate>
                  </Label>
                  <AvField id="controller-name" type="text" name="name" />
                </AvGroup>
                <AvGroup>
                  <Label id="modeLabel">
                    <Translate contentKey="rcraspiApp.controller.mode">Mode</Translate>
                  </Label>
                  <AvInput
                    id="controller-mode"
                    type="select"
                    className="form-control"
                    name="mode"
                    value={(!isNew && controllerEntity.mode) || 'OUTPUT'}
                  >
                    {/*<option value="INPUT">*/}
                      {/*<Translate contentKey="rcraspiApp.IO.INPUT" />*/}
                    {/*</option>*/}
                    <option value="OUTPUT">
                      <Translate contentKey="rcraspiApp.IO.OUTPUT" />
                    </option>
                  </AvInput>
                  <FormText color="muted">
                    <Translate contentKey="rcraspiApp.IO.message" />
                  </FormText>
                </AvGroup>
                <AvGroup>
                  <Label for="pin.name">
                    <Translate contentKey="rcraspiApp.controller.pin">Pin</Translate>
                  </Label>
                  <AvInput id="controller-pin" type="select" className="form-control" name="pinId">
                    <option value="" key="0" />
                    {pins
                      ? pins.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.name}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/controller" replace color="info">
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
  pins: storeState.pin.entities,
  controllerEntity: storeState.controller.entity,
  loading: storeState.controller.loading,
  updating: storeState.controller.updating,
  updateSuccess: storeState.controller.updateSuccess
});

const mapDispatchToProps = {
  getPins,
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
)(ControllerUpdate);
