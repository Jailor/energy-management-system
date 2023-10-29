import React, { useState } from 'react';
import PropTypes from 'prop-types';
import './styles/field-style.css';

const Field = (props) => {
  const [state, setState] = useState({
    focused: (props.locked && props.focused) || false,
    value: props.value || '',
    error: props.error || '',
    label: props.label || '',
  });

  const onChange = (event) => {
    const value = event.target.value;
    setState({ ...state, value, error: '' });
    return props.onChange(event);
  };

  const { focused, value, error, label } = state;
  const { id, locked } = props;
  const fieldClassName = `field ${(locked ? focused : focused || value) && 'focused'} ${locked && !focused && 'locked'}`;

  return (
    <div className={fieldClassName}>
      <input
        id={id}
        type="text"
        value={value}
        placeholder={label}
        onChange={onChange}
        onFocus={() => !locked && setState({ ...state, focused: true })}
        onBlur={() => !locked && setState({ ...state, focused: false })}
      />
      <label htmlFor={id} className={error && 'error'}>
        {error || label}
      </label>
    </div>
  );
};

Field.propTypes = {
  id: PropTypes.string.isRequired,
  locked: PropTypes.bool,
  focused: PropTypes.bool,
  value: PropTypes.string,
  error: PropTypes.string,
  label: PropTypes.string,
  onChange: PropTypes.func,
};

Field.defaultProps = {
  locked: false,
  focused: false,
  value: '',
  error: '',
  label: '',
};

export default Field;
