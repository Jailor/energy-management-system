
const minLengthValidator = (value, minLength) => {
    return value.length >= minLength;
};

const requiredValidator = value => {
    return value.trim() !== '';
};

const numberRangeValidator = (value, min, max) => {
    const intValue = parseInt(value, 10);
    return !isNaN(intValue) && intValue >= min && intValue <= max;
};

// const emailValidator = value => {
//     const re = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@(([[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
//     return re.test(String(value).toLowerCase());
// };

const validate = (value, rules) => {
    let isValid = true;

    for (let rule in rules) {

        switch (rule) {
            case 'minLength': isValid = isValid && minLengthValidator(value, rules[rule]);
                              break;

            case 'isRequired': isValid = isValid && requiredValidator(value);
                               break;
            case 'numberRange': isValid = isValid && numberRangeValidator(value, rules[rule].min, rules[rule].max);
                               break;

            default: isValid = true;
        }

    }

    return isValid;
};

export default validate;
