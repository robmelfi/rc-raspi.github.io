import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './netatmo.reducer';
import { INetatmo } from 'app/shared/model/netatmo.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface INetatmoUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface INetatmoUpdateState {
  isNew: boolean;
}

export class NetatmoUpdate extends React.Component<INetatmoUpdateProps, INetatmoUpdateState> {
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
      const { netatmoEntity } = this.props;
      const entity = {
        ...netatmoEntity,
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
    this.props.history.push('/entity/netatmo');
  };

  render() {
    const { netatmoEntity, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="rcraspiApp.netatmo.home.createOrEditLabel">
              <Translate contentKey="rcraspiApp.netatmo.home.createOrEditLabel">Create or edit a Netatmo</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : netatmoEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="netatmo-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="clientIdLabel" for="clientId">
                    <Translate contentKey="rcraspiApp.netatmo.clientId">Client Id</Translate>
                  </Label>
                  <AvField
                    id="netatmo-clientId"
                    type="text"
                    name="clientId"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="clientSecretLabel" for="clientSecret">
                    <Translate contentKey="rcraspiApp.netatmo.clientSecret">Client Secret</Translate>
                  </Label>
                  <AvField
                    id="netatmo-clientSecret"
                    type="text"
                    name="clientSecret"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="emailLabel" for="email">
                    <Translate contentKey="rcraspiApp.netatmo.email">Email</Translate>
                  </Label>
                  <AvField
                    id="netatmo-email"
                    type="email"
                    name="email"
                    placeholder={translate('global.form.email.placeholder')}
                    validate={{
                        required: { value: true, errorMessage: translate('global.messages.validate.email.required') },
                        minLength: { value: 5, errorMessage: translate('global.messages.validate.email.minlength') },
                        maxLength: { value: 254, errorMessage: translate('global.messages.validate.email.maxlength') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="passwordLabel" for="password">
                    <Translate contentKey="rcraspiApp.netatmo.password">Password</Translate>
                  </Label>
                  <AvField
                    id="netatmo-password"
                    type="text"
                    name="password"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="enabledLabel" check>
                    <AvInput default={'true'} id="netatmo-enabled" type="checkbox" className="form-control" name="enabled" />
                    <Translate contentKey="rcraspiApp.netatmo.enabled">Enabled</Translate>
                  </Label>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/netatmo" replace color="info">
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
  netatmoEntity: storeState.netatmo.entity,
  loading: storeState.netatmo.loading,
  updating: storeState.netatmo.updating,
  updateSuccess: storeState.netatmo.updateSuccess
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
)(NetatmoUpdate);
