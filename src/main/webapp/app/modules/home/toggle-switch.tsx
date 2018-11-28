import React from 'react';
import { Button, ButtonGroup } from 'reactstrap';

const toggleSwitch = props => {
  const content = (
    <ButtonGroup>
      <Button
        color="primary"
        onClick={props.on}
        disabled={props.status}>ON</Button>
      <Button
        color="primary"
        onClick={props.off}
        disabled={!props.status}>OFF</Button>
    </ButtonGroup>
  );
  return content;
};

export default toggleSwitch;
