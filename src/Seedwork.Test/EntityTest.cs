using System;
using Xunit;

namespace Seedwork {
    class TestEntity1: Entity {
    }

    class TestEntity2: Entity {
    }

    class TestEntity3: Entity {
        public TestEntity3(int Id): base() {
            base.Id = Id;
        }
    }

    public class EntityTest {
        [Fact]
        public void TwoEntitiesOfDifferentTypesAreNotEqual() {
            // Arrange
            Entity e1 = new TestEntity1();
            Entity e2 = new TestEntity2();

            // Act
            bool result = e1.Equals(e2);

            // Assert
            Assert.False(result);
        }

        [Fact]
        public void TwoEntitiesWithDifferentIdsAreNotEqual() {
            // Arrange
            Entity e1 = new TestEntity3(1);
            Entity e2 = new TestEntity3(2);

            // Act
            bool result = e1.Equals(e2);

            // Assert
            Assert.False(result);
        }

        [Fact]
        public void SameObjectIsEqual() {
            // Arrange
            Entity e1 = new TestEntity1();

            // Act
            bool result = e1.Equals(e1);

            // Assert
            Assert.True(result);
        }

        [Fact]
        public void ObjectIsNotEqualToNull() {
            // Arrange
            Entity e1 = new TestEntity1();

            // Act
            bool result = e1.Equals(null);

            // Assert
            Assert.False(result);
        }
    }
}